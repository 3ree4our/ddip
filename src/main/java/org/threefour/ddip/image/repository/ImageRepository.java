package org.threefour.ddip.image.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.threefour.ddip.image.domain.Image;
import org.threefour.ddip.image.domain.TargetType;

import java.util.List;

public interface ImageRepository extends JpaRepository<Image, Long> {
    List<Image> findByTargetTypeAndTargetId(TargetType targetType, Long id);
}
