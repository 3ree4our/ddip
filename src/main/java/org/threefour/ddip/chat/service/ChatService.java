package org.threefour.ddip.chat.service;

import org.threefour.ddip.chat.domain.Chat;
import org.threefour.ddip.chat.domain.dto.ChatRequestDTO;

public interface ChatService {
  Chat createChat(ChatRequestDTO dto);

  int getUnreadMessageCount(Long productId, Long ownerId);

  void markAsRead(Long productId, Long ownerId);

}
