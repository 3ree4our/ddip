package org.threefour.ddip.chat.service;

import org.threefour.ddip.chat.domain.Waiting;
import org.threefour.ddip.chat.domain.dto.WaitingRequestDTO;

public interface WaitingService {
  Long addWaiting(WaitingRequestDTO dto);

  Waiting getChatByProductId(Long productId);
}
