package org.threefour.ddip.image.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.threefour.ddip.image.domain.AddImagesRequest;
import org.threefour.ddip.image.service.ImageService;
import org.threefour.ddip.util.FormatConverter;

import java.util.List;

import static org.springframework.http.HttpStatus.NO_CONTENT;

@Controller
@RequestMapping("/image")
@RequiredArgsConstructor
public class ImageController {
  private final ImageService imageService;

  @PostMapping("/add")
  public ResponseEntity<Void> addImages(
          @RequestParam("images") List<MultipartFile> images,
          @RequestParam("targetType") String targetType,
          @RequestParam("targetId") String targetId
  ) {
    imageService.createImages(AddImagesRequest.from(images, targetType, targetId));

    return ResponseEntity.status(NO_CONTENT).build();
  }

  @PatchMapping("/designate-representative")
  public ResponseEntity<Void> designateRepresentativeImage(@RequestParam("imageId") String id) {
    imageService.designateRepresentativeImage(FormatConverter.parseToLong(id));

    return ResponseEntity.status(NO_CONTENT).build();
  }

  @DeleteMapping("/delete/{imageId}")
  public ResponseEntity<Void> deleteImage(@PathVariable("imageId") String id) {
    imageService.deleteImage(FormatConverter.parseToLong(id));

    return ResponseEntity.status(NO_CONTENT).build();
  }

  @PostMapping("/rollback-deletion/{imageId}")
  public ResponseEntity<Void> rollbackDeletion(@PathVariable("imageId") String id) {
    imageService.rollbackDeletion(FormatConverter.parseToLong(id));

    return ResponseEntity.status(NO_CONTENT).build();
  }
}
