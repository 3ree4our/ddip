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

  //SELECT * FROM Chat c
  //WHERE c.delete_yn = false
  //AND c.owner_id = :ownerId
  //AND c.id IN (
  //        SELECT MAX(c2.id)
  //FROM Chat c2
  //WHERE c2.delete_yn = false
  //AND c2.owner_id = :ownerId
  //GROUP BY c2.owner_id, c2.product_id
  //        )
  //ORDER BY c.owner_id;

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


  /*
  select c.product_id, c.message, p.title, (select name from member m join product p on m.id = p.seller_id where p.seller_id = 여기랑) nickname, c.sendDate from chat c join product p on c.product_id =p.id
  where c.delete_yn = false and c.owner_id = 여기;
  */
  //@Query("select c from Chat c where c.productId.id = :productId AND c.deleteYn = false")
  @Query("select new org.threefour.ddip.chat.domain.dto.ChatResponseDTO(c.productId.id, c.message, p.title, m.nickName, c.sendDate) " +
          "from Chat c join Product p on c.productId.id = p.id " +
          "join Member m on m.id = p.seller.id " +
          "where c.productId.id = :productId AND c.deleteYn = false")
  List<ChatResponseDTO> findAllChatByProductId(@Param("productId") Long productId);

}