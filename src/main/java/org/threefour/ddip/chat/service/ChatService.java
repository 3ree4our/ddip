package org.threefour.ddip.chat.service;

import org.threefour.ddip.chat.domain.Chat;
import org.threefour.ddip.chat.domain.dto.ChatRequestDTO;

public interface ChatService {
  Chat createChat(ChatRequestDTO dto);
}
