package org.threefour.ddip.image.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.threefour.ddip.image.domain.Image;
import org.threefour.ddip.image.domain.RepresentativeImagesRequest;
import org.threefour.ddip.image.domain.TargetType;
import org.threefour.ddip.image.repository.ImageRepository;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class ImageLocalServiceImpl implements ImageService {

  private final ImageRepository imageRepository;

  @Value("${image.upload.dir}")
  private String baseUploadDir;

  public List<Long> saveImages(TargetType targetType, Long targetId, List<MultipartFile> imageRequests) {
    List<Long> saveImagesId = new ArrayList<>();

    for (MultipartFile imageRequest : imageRequests) {
      try {
        String fileName = generateFileName(imageRequest.getOriginalFilename());
        String filePath = generateFilePath(fileName);
        String fullPath = Paths.get(baseUploadDir, filePath).toString();

        File file = new File(fullPath);
        if (!file.getParentFile().exists()) {
          file.getParentFile().mkdirs();
        }

        imageRequest.transferTo(file);

        Image image = Image.of(targetType, targetId, filePath);
        saveImagesId.add(imageRepository.save(image).getId());
      } catch (IOException ie) {
        ie.printStackTrace();
      }
    }
    return saveImagesId;
  }

  @Override
  public void createImages(TargetType targetType, Long targetId, List<MultipartFile> imageRequests) {
  }

  @Override
  public List<Image> getImages(TargetType targetType, Long targetId) {
    return List.of();
  }

  @Override
  public List<Image> getRepresentativeImages(RepresentativeImagesRequest representativeImagesRequest) {
    return List.of();
  }

  @Override
  public void deleteImage(Long id) {

  }

  private String generateFileName(String originalFilename) {
    String timeStamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
    String extention = originalFilename.substring(originalFilename.lastIndexOf("."));
    return timeStamp + "_" + UUID.randomUUID().toString().substring(0, 8) + extention;
  }

  private String generateFilePath(String filename) {
    LocalDateTime now = LocalDateTime.now();
    String filePath = now.format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
    return String.format("%s/%s", filePath, filename);
  }

  public List<Long> getImageByChatId(Long chatId) {
    List<Long> imagesUrl = new ArrayList<>();
    List<Image> byTargetId = imageRepository.findByTargetId(chatId);
    for (Image image : byTargetId) {
      imagesUrl.add(image.getId());
    }

    return imagesUrl;
  }

  public Image getImageById(Long imageId) {
    return imageRepository.findById(imageId).orElse(null);
  }

  public String getFileBasePath() {
    return baseUploadDir;
  }
}
