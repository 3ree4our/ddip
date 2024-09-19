package org.threefour.ddip.image.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.threefour.ddip.image.domain.AddImagesRequest;
import org.threefour.ddip.image.domain.Image;
import org.threefour.ddip.image.domain.RepresentativeImagesRequest;
import org.threefour.ddip.image.domain.TargetType;
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

    @Value("${cloud.aws.s3.bucket-name}")
    private String s3BucketName;

    @Override
    @Transactional(isolation = READ_COMMITTED, timeout = 10)
    public void createImages(AddImagesRequest addImagesRequest) {
        List<MultipartFile> imagesToUpload = addImagesRequest.getImages();
        TargetType targetType = FormatConverter.parseToTargetType(addImagesRequest.getTargetType());
        Long targetId = FormatConverter.parseToLong(addImagesRequest.getTargetId());

        List<Image> images = new ArrayList<>();
        for (int i = 0; i < imagesToUpload.size(); i++) {
            images.add(createImage(imagesToUpload.get(i), targetType, targetId, i == 0));
        }

        imageRepository.saveAll(images);
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
        File convertedFile
                = new File(String.format("%s/%s", System.getProperty("java.io.tmpdir"), image.getOriginalFilename()));
        try (FileOutputStream fos = new FileOutputStream(convertedFile)) {
            fos.write(image.getBytes());
        }
        return convertedFile;
    }

    @Override
    @Transactional(isolation = READ_COMMITTED, readOnly = true, timeout = 10)
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
    public void designateRepresentativeImage(Long id) {
        Image previousRepresentativeImage = imageRepository.findByRepresentativeYnTrueAndDeleteYnFalse()
                .orElse(null);
        if (FormatValidator.hasValue(previousRepresentativeImage)) {
            previousRepresentativeImage.cancelRepresentative();
        }

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
    public void deleteImage(Long id) {
        Image image = getImage(id);
        image.delete();
        imageRepository.save(image);
    }

    @Override
    @Transactional(isolation = READ_UNCOMMITTED, timeout = 10)
    public void rollbackDeletion(Long id) {
        Image image = imageRepository.findById(id)
                .orElseThrow(() -> new ImageNotFoundException(String.format(IMAGE_NOT_FOUND_EXCEPTION_MESSAGE, id)));
        image.undelete();
        imageRepository.save(image);
    }
}
