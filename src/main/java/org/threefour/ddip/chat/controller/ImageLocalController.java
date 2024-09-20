package org.threefour.ddip.chat.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.threefour.ddip.image.domain.Image;
import org.threefour.ddip.image.domain.TargetType;
import org.threefour.ddip.image.service.ImageLocalServiceImpl;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/images")
@RequiredArgsConstructor
public class ImageLocalController {

  private final ImageLocalServiceImpl imageLocalService;

  @PostMapping("/upload")
  public ResponseEntity<List<Long>> uploadImage(@RequestParam("files") List<MultipartFile> files,
                                                @RequestParam("chatId") Long chatId) {
    List<Long> longs = imageLocalService.saveImages(TargetType.CHATTING, chatId, files);

    return ResponseEntity.ok().body(longs);
  }

  @GetMapping("/{imageId}")
  public ResponseEntity<Resource> getImageByImageId(@PathVariable Long imageId) throws IOException {
    Image image = imageLocalService.getImageById(imageId);

    if (image == null) return ResponseEntity.notFound().build();

    String fileBasePath = imageLocalService.getFileBasePath();
    Path imagePath = Paths.get(fileBasePath, image.getS3Url());

    Resource resource = new FileSystemResource(imagePath.toFile());

    if (!resource.exists()) return ResponseEntity.notFound().build();

    String contentType = Files.probeContentType(imagePath.getFileName());

    return ResponseEntity.ok()
            .contentType(MediaType.parseMediaType(contentType))
            .body(resource);
  }

  @GetMapping("/chat/{chatId}")
  public ResponseEntity<List<Long>> getImageByChatId(@PathVariable Long chatId) {
    List<Long> imageByChatId = imageLocalService.getImageByChatId(chatId);
    System.out.println("chatId: " + chatId);
    System.out.println("imageByChatId: " + imageByChatId);
    return ResponseEntity.ok().body(imageByChatId);
  }
}
