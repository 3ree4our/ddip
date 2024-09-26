package org.threefour.ddip.image.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@NoArgsConstructor
@Getter
@Setter
public class AddImagesRequest {
    List<MultipartFile> images;
    private String targetType;
    private String targetId;

    private AddImagesRequest(List<MultipartFile> images, String targetType, String targetId) {
        this.images = images;
        this.targetType = targetType;
        this.targetId = targetId;
    }

    public static AddImagesRequest from(List<MultipartFile> images, String targetType, String targetId) {
        return new AddImagesRequest(images, targetType, targetId);
    }
}
