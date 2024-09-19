package org.threefour.ddip.chat.service;

import org.threefour.ddip.chat.domain.Waiting;
import org.threefour.ddip.chat.domain.dto.WaitingRequestDTO;
import org.threefour.ddip.chat.domain.dto.WaitingResponseDTO;

import java.util.List;

public interface WaitingService {
  WaitingResponseDTO createWatiting(Long productId, Long senderId);

  boolean isProductAvailableForChat(Long productId);

  void acceptWaiting(Long waitingId);

  void rejectWaiting(Long waitingId);

  void startChat(Long waitingId);

  List<WaitingResponseDTO> getWaitingListForProduct(Long productId);

  List<WaitingResponseDTO> getWaitingListForSender(Long sanderId);

  Long getWaitingCountForProduct(Long productId);
}
