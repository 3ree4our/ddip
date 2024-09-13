package org.threefour.ddip.chat.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.threefour.ddip.chat.domain.Waiting;
import org.threefour.ddip.chat.domain.dto.WaitingRequestDTO;
import org.threefour.ddip.chat.repository.WaitingRepository;
import org.threefour.ddip.product.domain.Product;
import org.threefour.ddip.product.repository.ProductRepository;

import javax.persistence.EntityNotFoundException;

@Service
@RequiredArgsConstructor
public class WaitingServiceImpl implements WaitingService {

  private final WaitingRepository waitingRepository;
  private final ProductRepository productRepository;

  @Override
  public Waiting getChatByProductId(Long productId) {
    Waiting byProductId = waitingRepository.findByProductId(productId).orElseThrow();
    return byProductId;
  }

 /* public Long addWaiting(WaitingRequestDTO dto) {
    Product product = productRepository.findById(dto.getProductId())
            .orElseThrow(() -> new EntityNotFoundException("Product not found"));

    boolean isPossible = !waitingRepository.findByProductId(dto.getProductId()).isPresent();

    Waiting waiting = Waiting.builder()
            .productId(product)
            .senderId(dto.getSenderId())
            .isPossible(isPossible)
            .build();

    return waitingRepository.save(waiting).getId();
  }*/

  @Override
  public Long addWaiting(WaitingRequestDTO dto) {
    Product product = productRepository.findById(dto.getProductId())
            .orElseThrow(() -> new EntityNotFoundException("Product not found"));

    boolean isPossible = !waitingRepository.findByProductId(dto.getProductId()).isPresent();

    Waiting waiting = Waiting.builder()
            .productId(product)
            .senderId(dto.getSender())
            .isPossible(isPossible)
            .build();

    return waitingRepository.save(waiting).getId();
  }

}
