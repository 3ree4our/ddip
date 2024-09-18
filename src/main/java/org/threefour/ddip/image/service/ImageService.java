package org.threefour.ddip.image.service;

import org.threefour.ddip.image.domain.AddImagesRequest;
import org.threefour.ddip.image.domain.Image;
import org.threefour.ddip.image.domain.RepresentativeImagesRequest;
import org.threefour.ddip.image.domain.TargetType;

import java.util.List;

public interface ImageService {
    void createImages(AddImagesRequest addImagesRequest);

    List<Image> getImages(TargetType targetType, Long targetId);

    List<Image> getRepresentativeImages(RepresentativeImagesRequest representativeImagesRequest);

    void designateRepresentativeImage(Long id);

    void deleteImage(Long id);

    void rollbackDeletion(Long id);
}
