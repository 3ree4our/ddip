package org.threefour.ddip.chat.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.threefour.ddip.image.domain.AddImagesRequest;
import org.threefour.ddip.image.domain.Image;
import org.threefour.ddip.image.domain.TargetType;
import org.threefour.ddip.image.service.ImageLocalServiceImpl;
import org.threefour.ddip.image.service.ImageService;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/images")
@RequiredArgsConstructor
public class ImageLocalController {

  private final ImageLocalServiceImpl imageLocalService;
  private final ImageService imageService;
  /*
  @PostMapping("/upload")
  public ResponseEntity<List<Long>> uploadImage(@RequestParam("files") List<MultipartFile> files,
                                                @RequestParam("chatId") Long chatId) {
    List<Long> longs = imageLocalService.saveImages(TargetType.CHATTING, chatId, files);

    return ResponseEntity.ok().body(longs);
  }
  */

  @PostMapping("/upload")
  public ResponseEntity<List<Image>> uploadImage(@RequestParam("files") List<MultipartFile> files,
                                                 @RequestParam("chatId") Long chatId) {
    AddImagesRequest from = AddImagesRequest.from(files, TargetType.CHATTING.name(), String.valueOf(chatId));

    List<Image> images = imageService.createImages(from);

    return ResponseEntity.ok().body(images);
  }

  @GetMapping("/{imageId}")
  public ResponseEntity<List<String>> getImageByImageIdWithS3(@PathVariable Long imageId) throws IOException {
    List<Image> images = imageService.getImages(TargetType.CHATTING, imageId);
    List<String> resources = new ArrayList<>();

    if (images.size() < 1) return ResponseEntity.notFound().build();

    for (Image image : images) resources.add(URLEncoder.encode(image.getS3Url(), StandardCharsets.UTF_8.toString()));

    return ResponseEntity.ok()
            .body(resources);
  }

 /* @GetMapping("/{imageId}")
  public ResponseEntity<Resource> getImageByImageIdWithLocal(@PathVariable Long imageId) throws IOException {
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
  }*/

  @GetMapping("/chat/{chatId}")
  public ResponseEntity<List<Long>> getImageByChatId(@PathVariable Long chatId) {
    List<Long> imageByChatId = imageLocalService.getImageByChatId(chatId);
    System.out.println("chatId: " + chatId);
    System.out.println("imageByChatId: " + imageByChatId);
    return ResponseEntity.ok().body(imageByChatId);
  }
}
