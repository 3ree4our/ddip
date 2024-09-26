package org.threefour.ddip.image.service;

import org.threefour.ddip.image.domain.*;

import java.util.List;

public interface ImageService {
    List<Image> createImages(AddImagesRequest addImagesRequest);

    List<Image> getImages(TargetType targetType, Long targetId);

    List<Image> getRepresentativeImages(RepresentativeImagesRequest representativeImagesRequest);

    void designateRepresentativeImage(DesignageRepresentativeImageRequest request);

    void deleteImage(Long id);

    void rollbackDeletion(Long id);
}
