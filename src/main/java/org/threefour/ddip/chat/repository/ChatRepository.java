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

  @Query("SELECT new org.threefour.ddip.chat.domain.dto.ChatroomResponseDTO( " +
          " c.productId.id, c.message, MAX(c.sendDate), c.owner, p.seller) " +
          "from Chat c join Product p on c.productId.id = p.id " +
          "where c.deleteYn = false " +
          "AND c.productId.id = :productId " +
          "group by c.productId.id"
  )
  ChatroomResponseDTO findChatByProductId(@Param("productId") Long productId);

  @Query("SELECT new org.threefour.ddip.chat.domain.dto.ChatroomResponseDTO( " +
          " c.productId.id, c.message, MAX(c.sendDate), c.owner, p.seller) " +
          "from Chat c join Product p on c.productId.id = p.id " +
          "where c.deleteYn = false " +
          "AND c.owner.id = :ownerId " +
          "group by c.productId.id"
  )
  List<ChatroomResponseDTO> findAllChatByOwnerId(@Param("ownerId") Long ownerId);

  @Query("select new org.threefour.ddip.chat.domain.dto.ChatResponseDTO(c.productId.id, c.message, p.title, m.nickName, c.sendDate) " +
          "from Chat c join Product p on c.productId.id = p.id " +
          "join Member m on m.id = p.seller.id " +
          "where c.productId.id = :productId AND c.deleteYn = false")
  List<ChatResponseDTO> findAllChatByProductId(@Param("productId") Long productId);

}