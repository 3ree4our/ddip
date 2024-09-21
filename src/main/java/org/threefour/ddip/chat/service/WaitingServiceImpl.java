package org.threefour.ddip.chat.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.threefour.ddip.chat.domain.Waiting;
import org.threefour.ddip.chat.domain.WaitingStatus;
import org.threefour.ddip.chat.domain.dto.WaitingRequestDTO;
import org.threefour.ddip.chat.domain.dto.WaitingResponseDTO;
import org.threefour.ddip.chat.repository.WaitingRepository;
import org.threefour.ddip.member.domain.Member;
import org.threefour.ddip.member.repository.MemberRepository;
import org.threefour.ddip.product.domain.Product;
import org.threefour.ddip.product.repository.ProductRepository;
import org.threefour.ddip.product.service.ProductService;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class WaitingServiceImpl implements WaitingService {

  private final WaitingRepository waitingRepository;
  private final ProductRepository productRepository;
  private final MemberRepository memberRepository;

  public WaitingResponseDTO createWatiting(Long productId, Long senderId) {
    Product product = productRepository.findById(productId).orElseThrow(EntityNotFoundException::new);
    Member sender = memberRepository.findById(senderId).orElseThrow(EntityNotFoundException::new);

    Optional<Waiting> existingWaiting = waitingRepository.findByProductAndSender(product, sender);
    if (existingWaiting.isPresent()) {
      throw new IllegalStateException("Waiting already exists for this product{" + product.getId() + "} and sender{" + sender.getId() + "}");
    }

    Waiting waiting = Waiting.builder()
            .product(product)
            .sender(sender)
            .status(WaitingStatus.WAITING)
            .build();

    Waiting savedWaiting = waitingRepository.save(waiting);

    return WaitingResponseDTO.from(savedWaiting);
  }

  public boolean isProductAvailableForChat(Long productId) {
    Product product = productRepository.findById(productId).orElseThrow(EntityNotFoundException::new);
    return waitingRepository.findByProductAndStatus(product, WaitingStatus.IN_PROGRESS).isEmpty();
  }

  public void acceptWaiting(Long waitingId) {
    Waiting waiting = waitingRepository.findById(waitingId).orElseThrow(EntityNotFoundException::new);
    waiting.updateStatus(WaitingStatus.ACCEPTED);
    waitingRepository.save(waiting);
  }

  public void rejectWaiting(Long waitingId) {
    Waiting waiting = waitingRepository.findById(waitingId).orElseThrow(EntityNotFoundException::new);
    waiting.updateStatus(WaitingStatus.REJECTED);
    waitingRepository.save(waiting);
  }

  public void startChat(Long waitingId) {
    Waiting waiting = waitingRepository.findById(waitingId).orElseThrow(EntityNotFoundException::new);
    waiting.updateStatus(WaitingStatus.IN_PROGRESS);
    waitingRepository.save(waiting);
  }

  public List<WaitingResponseDTO> getWaitingListForProduct(Long productId) {
    Product product = productRepository.findById(productId).orElseThrow(EntityNotFoundException::new);
    return waitingRepository.findByProductOrderByCreatedAtAsc(product)
            .stream().map(WaitingResponseDTO::from)
            .collect(Collectors.toList());
  }

  public List<WaitingResponseDTO> getWaitingListForSender(Long sanderId) {
    Member member = memberRepository.findById(sanderId).orElseThrow(EntityNotFoundException::new);
    return waitingRepository.findBySenderOrderByCreatedAtDesc(member)
            .stream().map(WaitingResponseDTO::from)
            .collect(Collectors.toList());
  }

  public Long getWaitingCountForProduct(Long productId) {
    Product product = productRepository.findById(productId).orElseThrow(EntityNotFoundException::new);
    return waitingRepository.countByProductAndStatus(product, WaitingStatus.WAITING);
  }
}
