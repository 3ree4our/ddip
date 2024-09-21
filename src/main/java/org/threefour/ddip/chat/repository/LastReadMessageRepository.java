package org.threefour.ddip.chat.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.threefour.ddip.chat.domain.dto.LastReadMessage;
import org.threefour.ddip.chat.domain.dto.LastReadMessageId;

import java.util.Optional;

@Repository
public interface LastReadMessageRepository extends JpaRepository<LastReadMessage, LastReadMessageId> {

  @Query("SELECT MAX(c.id) FROM Chat c WHERE c.productId.id = :productId")
  Long findLastMessageIdByProductId(@Param("productId") Long productId);

  @Query("SELECT COUNT(c) FROM Chat c WHERE c.productId.id = :productId AND c.id > :lastReadId")
  int countUnreadMessages(@Param("productId") Long productId, @Param("lastReadId") Long lastReadId);

  Optional<LastReadMessage> findByIdProductIdAndIdOwnerId(Long productId, Long ownerId);
}
