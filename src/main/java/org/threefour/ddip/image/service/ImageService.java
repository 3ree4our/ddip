package org.threefour.ddip.image.service;

import org.springframework.web.multipart.MultipartFile;
import org.threefour.ddip.image.domain.Image;
import org.threefour.ddip.image.domain.RepresentativeImagesRequest;
import org.threefour.ddip.image.domain.TargetType;

import java.util.List;

public interface ImageService {
    void createImages(TargetType targetType, Long targetId, List<MultipartFile> imageRequests);

    List<Image> getImages(TargetType targetType, Long targetId);

    List<Image> getRepresentativeImages(RepresentativeImagesRequest representativeImagesRequest);

    void deleteImage(Long id);
}
