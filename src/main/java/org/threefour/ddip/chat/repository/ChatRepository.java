package org.threefour.ddip.chat.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.threefour.ddip.chat.domain.Chat;
import org.threefour.ddip.chat.domain.dto.ChatroomResponseDTO;

import java.util.List;

@Repository
public interface ChatRepository extends JpaRepository<Chat, Long> {

  @Query("SELECT new org.threefour.ddip.chat.domain.dto.ChatroomResponseDTO(" +
          "c.id, c.message,c.owner.id, p.id, p.title) " +
          "FROM Chat c " +
          "JOIN c.productId p " +
          "WHERE c.deleteYn = false " +
          "AND c.owner.id = :ownerId " +
          "AND c.id IN (" +
          "    SELECT MAX(c2.id) " +
          "    FROM Chat c2 " +
          "    WHERE c2.deleteYn = false " +
          "    AND c2.owner.id = :ownerId " +
          "    GROUP BY c2.owner.id, c2.productId.id" +
          ") " +
          "ORDER BY p.id, c.owner.id")
  List<ChatroomResponseDTO> findAllChatByReceiverId(@Param("ownerId") Long ownerId);

  @Query("select c from Chat c where c.productId.id = :productId AND c.deleteYn = false")
  List<Chat> findAllChatByProductId(@Param("productId") Long productId);

}

