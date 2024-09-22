package org.threefour.ddip.image.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.threefour.ddip.image.domain.*;
import org.threefour.ddip.image.exception.ImageNotFoundException;
import org.threefour.ddip.image.exception.S3UploadFailedException;
import org.threefour.ddip.image.repository.ImageRepository;
import org.threefour.ddip.util.FormatConverter;
import org.threefour.ddip.util.FormatValidator;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.transaction.annotation.Isolation.*;
import static org.threefour.ddip.image.exception.ExceptionMessage.*;

@Service
@RequiredArgsConstructor
public class ImageServiceImpl implements ImageService {
  private final ImageRepository imageRepository;
  private final S3Client s3Client;

  private static final String TLS_HTTP_PROTOCOL = "https://";
  private static final String S3_ADDRESS = "s3.amazonaws.com";
  private static final String CACHE_VALUE = "images";

  private static final String CREATE_IMAGE_KEY = "#addImagesRequest.targetId";
  private static final String CREATE_KEY_CONDITION = "#addImagesRequest != null && #addImagesRequest.targetId != null";

  private static final String GET_IMAGE_KEY = "#targetId";
  private static final String GET_KEY_CONDITION = "#targetId != null";

  private static final String DELETE_IMAGE_KEY = "#id";
  private static final String DELETE_KEY_CONDITION = "#id != null";

  @Value("${cloud.aws.s3.bucket-name}")
  private String s3BucketName;

  @Override
  @Transactional(isolation = READ_COMMITTED, timeout = 10)
  @CachePut(key = CREATE_IMAGE_KEY, condition = CREATE_KEY_CONDITION, value = CACHE_VALUE)
  public List<Image> createImages(AddImagesRequest addImagesRequest) {
    List<MultipartFile> imagesToUpload = addImagesRequest.getImages();
    TargetType targetType = FormatConverter.parseToTargetType(addImagesRequest.getTargetType());
    Long targetId = FormatConverter.parseToLong(addImagesRequest.getTargetId());

    List<Image> existingImages = imageRepository.findByTargetTypeAndTargetIdAndDeleteYnFalse(targetType, targetId);
    boolean isRepresentativeImageExistent = FormatValidator.hasValue(existingImages);

    List<Image> newImages = new ArrayList<>();
    for (int i = 0; i < imagesToUpload.size(); i++) {
      newImages.add(
              createImage(imagesToUpload.get(i), targetType, targetId, i == 0 && !isRepresentativeImageExistent)
      );
    }

    List<Image> allImages = new ArrayList<>(existingImages);
    allImages.addAll(imageRepository.saveAll(newImages));

    return allImages;
  }

  private Image createImage(MultipartFile image, TargetType targetType, Long targetId, boolean isRepresentative) {
    return Image.of(targetType, targetId, uploadToS3(image, targetType, targetId), isRepresentative);
  }

  private String uploadToS3(MultipartFile image, TargetType targetType, Long productId) {
    String originalFilename = image.getOriginalFilename();
    long ms = System.currentTimeMillis();
    String key = String.format("%s/%d/%s_%s", targetType.toString().toLowerCase(), productId, ms, originalFilename);

    try {
      File tempFile = convertMultipartFileToFile(image);
      s3Client.putObject(PutObjectRequest.builder()
              .bucket(s3BucketName)
              .key(key)
              .build(), Paths.get(tempFile.getPath()));
      tempFile.delete();
    } catch (IOException ie) {
      throw new S3UploadFailedException(S3_UPLOAD_FAILED_EXCEPTION_MESSAGE, ie);
    }

    return String.format("%s%s.%s/%s", TLS_HTTP_PROTOCOL, s3BucketName, S3_ADDRESS, key);
  }

  private File convertMultipartFileToFile(MultipartFile image) throws IOException {
    File convertedFile = new File(String.format("%s/%s", System.getProperty("java.io.tmpdir"), image.getOriginalFilename()));
    try (FileOutputStream fos = new FileOutputStream(convertedFile)) {
      fos.write(image.getBytes());
    }
    return convertedFile;
  }

  @Override
  @Transactional(isolation = READ_COMMITTED, readOnly = true, timeout = 10)
  @Cacheable(key = GET_IMAGE_KEY, condition = GET_KEY_CONDITION, value = CACHE_VALUE)
  public List<Image> getImages(TargetType targetType, Long targetId) {
    return imageRepository.findByTargetTypeAndTargetIdAndDeleteYnFalse(targetType, targetId);
  }

  @Override
  @Transactional(isolation = REPEATABLE_READ, readOnly = true, timeout = 20)
  public List<Image> getRepresentativeImages(RepresentativeImagesRequest representativeImagesRequest) {
    List<Image> images = new ArrayList<>();
    TargetType targetType = representativeImagesRequest.getTargetType();

    for (int i = 0; i < representativeImagesRequest.size(); i++) {
      Long targetId = representativeImagesRequest.get(i);
      images.add(
              imageRepository
                      .findByTargetTypeAndTargetIdAndRepresentativeYnTrueAndDeleteYnFalse(targetType, targetId)
                      .orElse(
                              imageRepository
                                      .findFirstByTargetTypeAndTargetIdAndDeleteYnFalseOrderByCreatedAt(
                                              targetType, targetId
                                      )
                                      .orElseThrow(() -> new ImageNotFoundException(String.format(
                                              TARGET_IMAGE_NOT_FOUND_EXCEPTION_MESSAGE, targetType, targetId
                                      )))
                      )
      );
    }

    return images;
  }

  @Override
  public void designateRepresentativeImage(DesignageRepresentativeImageRequest request) {
    TargetType targetType = FormatConverter.parseToTargetType(request.getTargetType());
    Long targetId = FormatConverter.parseToLong(request.getTargetId());
    Image previousRepresentativeImage = imageRepository
            .findByTargetTypeAndTargetIdAndRepresentativeYnTrueAndDeleteYnFalse(targetType, targetId)
            .orElse(null);

    if (FormatValidator.hasValue(previousRepresentativeImage)) {
      previousRepresentativeImage.cancelRepresentative();
    }

    Long id = FormatConverter.parseToLong(request.getId());
    Image imageToRepresentative = getImage(id);
    imageToRepresentative.designateRepresentative();
    imageRepository.save(imageToRepresentative);
  }

  private Image getImage(Long id) {
    return imageRepository.findByIdAndDeleteYnFalse(id)
            .orElseThrow(() -> new ImageNotFoundException(String.format(IMAGE_NOT_FOUND_EXCEPTION_MESSAGE, id)));
  }

  @Override
  @Transactional(isolation = READ_UNCOMMITTED, timeout = 10)
  @CacheEvict(key = DELETE_IMAGE_KEY, condition = DELETE_KEY_CONDITION, value = CACHE_VALUE)
  public void deleteImage(Long id) {
    Image image = getImage(id);
    image.delete();
    imageRepository.save(image);
  }

  @Override
  @Transactional(isolation = READ_UNCOMMITTED, timeout = 10)
  @CachePut(key = DELETE_IMAGE_KEY, condition = DELETE_KEY_CONDITION, value = CACHE_VALUE)
  public void rollbackDeletion(Long id) {
    Image image = imageRepository.findById(id)
            .orElseThrow(() -> new ImageNotFoundException(String.format(IMAGE_NOT_FOUND_EXCEPTION_MESSAGE, id)));
    image.undelete();
    imageRepository.save(image);
  }
}
