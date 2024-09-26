package org.threefour.ddip.chat.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.threefour.ddip.chat.domain.Chat;
import org.threefour.ddip.chat.domain.dto.ChatResponseDTO;
import org.threefour.ddip.chat.domain.dto.ChatroomResponseDTO;
import org.threefour.ddip.deal.domain.Deal;

import java.util.List;

@Repository
public interface ChatRepository extends JpaRepository<Chat, Long> {
  @Query("SELECT new org.threefour.ddip.chat.domain.dto.ChatroomResponseDTO(" +
          "c.productId.id, c.message, c.sendDate, c.owner, p.seller) " +
          "FROM Chat c " +
          "JOIN Product p on c.productId.id = p.id " +
          "WHERE c.deleteYn = false " +
          "AND c.productId.id = :productId " +
          "AND c.sendDate = (" +
          "    SELECT MAX(c2.sendDate) " +
          "    FROM Chat c2 " +
          "    WHERE c2.productId.id = c.productId.id " +
          "    AND c2.deleteYn = false" +
          ")")
  ChatroomResponseDTO findChatByProductId(@Param("productId") Long productId);

  @Query("SELECT new org.threefour.ddip.chat.domain.dto.ChatroomResponseDTO(" +
          "c.productId.id, c.message, c.sendDate, c.owner, " +
          "CASE WHEN p IS NULL THEN c.productId.seller ELSE p.seller END) " +
          "FROM Chat c " +
          "LEFT JOIN Product p ON c.productId.id = p.id " +
          "WHERE c.deleteYn = false " +
          "AND (c.owner.id = :ownerId OR c.productId.seller.id = :ownerId OR EXISTS (" +
          "    SELECT 1 FROM Chat c3 " +
          "    WHERE c3.productId.id = c.productId.id " +
          "    AND c3.owner.id = :ownerId" +
          ")) " +
          "AND c.sendDate = (" +
          "    SELECT MAX(c2.sendDate) " +
          "    FROM Chat c2 " +
          "    WHERE c2.productId.id = c.productId.id " +
          "    AND c2.deleteYn = false" +
          ")")
  List<ChatroomResponseDTO> findAllChatByOwnerIdOrSellerId(@Param("ownerId") Long ownerId);

  @Query("select new org.threefour.ddip.chat.domain.dto.ChatResponseDTO(c.id, c.productId.id, c.message, p.title,p.seller, c.owner, c.sendDate) " +
          "from Chat c join Product p on c.productId.id = p.id " +
          "join Member m on m.id = p.seller.id " +
          "where c.productId.id = :productId AND c.deleteYn = false " +
          "order by c.id")
  List<ChatResponseDTO> findAllChatByProductId(@Param("productId") Long productId);

  @Query("SELECT new org.threefour.ddip.chat.domain.dto.ChatroomResponseDTO(" +
          "c.productId.id, c.message, c.sendDate, c.owner, c.productId.seller) " +
          "FROM Chat c " +
          "WHERE c.productId.id = :productId " +
          "AND c.deleteYn = false " +
          "AND c.sendDate = (SELECT MAX(c2.sendDate) FROM Chat c2 WHERE c2.productId.id = :productId AND c2.deleteYn = false)")
  ChatroomResponseDTO findLatestChatByProductId(@Param("productId") Long productId);
}