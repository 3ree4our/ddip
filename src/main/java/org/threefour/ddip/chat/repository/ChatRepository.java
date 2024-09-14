package org.threefour.ddip.chat.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.threefour.ddip.chat.domain.Chat;
import org.threefour.ddip.chat.domain.dto.ChatResponseDTO;
import org.threefour.ddip.chat.domain.dto.ChatroomResponseDTO;

import java.util.List;

@Repository
public interface ChatRepository extends JpaRepository<Chat, Long> {

  @Query("SELECT new org.threefour.ddip.chat.domain.dto.ChatroomResponseDTO(" +
          "c.productId.id, c.message, c.sendDate) " +
          "from Chat c " +
          "WHERE c.deleteYn = false " +
          "AND c.owner.id = :ownerId " +
          "AND c.id IN (" +
          "    SELECT MAX(c2.id) " +
          "    FROM Chat c2 " +
          "    WHERE c2.deleteYn = false " +
          "    AND c2.owner.id = :ownerId " +
          "    GROUP BY c2.owner.id, c2.productId.id" +
          ") " +
          "ORDER BY c.owner.id")
  List<ChatroomResponseDTO> findAllChatByReceiverId(@Param("ownerId") Long ownerId);


  @Query("select new org.threefour.ddip.chat.domain.dto.ChatResponseDTO(c.productId.id, c.message, p.title, m.nickName, c.sendDate) " +
          "from Chat c join Product p on c.productId.id = p.id " +
          "join Member m on m.id = p.seller.id " +
          "where c.productId.id = :productId AND c.deleteYn = false")
  List<ChatResponseDTO> findAllChatByProductId(@Param("productId") Long productId);

}