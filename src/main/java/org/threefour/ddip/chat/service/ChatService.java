package org.threefour.ddip.chat.service;

import org.threefour.ddip.chat.domain.Chat;
import org.threefour.ddip.chat.domain.dto.ChatRequestDTO;
import org.threefour.ddip.chat.domain.dto.ChatResponseDTO;
import org.threefour.ddip.chat.domain.dto.ChatroomResponseDTO;
import org.threefour.ddip.chat.domain.dto.ProductResponseDTO;

import java.util.List;

public interface ChatService {
  Long createChat(ChatRequestDTO dto);

  ChatroomResponseDTO findChatByProductId(Long productId);

  List<ChatroomResponseDTO> findAllChatByOwnerId(Long ownerId);

  List<ChatResponseDTO> findAllChatByProductId(Long productId);

  int getUnreadMessageCount(Long productId, Long ownerId);

  void markAsRead(Long productId, Long ownerId);

  //TODO 추후 삭제
  ProductResponseDTO getProductByProductId(Long productId);

  //TODO 추후 삭제
  List<ProductResponseDTO> getAllProductBySellerId(Long sellerId);
}
