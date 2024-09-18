package org.threefour.ddip.image.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.threefour.ddip.image.domain.Image;
import org.threefour.ddip.image.domain.TargetType;

import java.util.List;
import java.util.Optional;

public interface ImageRepository extends JpaRepository<Image, Long> {
  List<Image> findByTargetTypeAndTargetIdAndDeleteYnFalse(TargetType targetType, Long id);

  Optional<Image> findByIdAndDeleteYnFalse(Long id);

  Optional<Image> findFirstByTargetTypeAndTargetIdAndDeleteYnFalseOrderByCreatedAt(
          TargetType targetType, Long targetId
  );

  // 추후 삭제
  List<Image> findByTargetId(Long targetId);
}
