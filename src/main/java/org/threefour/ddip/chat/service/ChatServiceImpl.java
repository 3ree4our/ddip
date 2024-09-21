package org.threefour.ddip.chat.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.threefour.ddip.chat.domain.Chat;
import org.threefour.ddip.chat.domain.dto.*;
import org.threefour.ddip.chat.repository.ChatRepository;
import org.threefour.ddip.chat.repository.LastReadMessageRepository;
import org.threefour.ddip.member.domain.Member;
import org.threefour.ddip.member.repository.MemberRepository;
import org.threefour.ddip.product.domain.Product;
import org.threefour.ddip.product.repository.ProductRepository;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class ChatServiceImpl implements ChatService {

  private final ChatRepository chatRepository;
  private final MemberRepository memberRepository;
  private final ProductRepository productRepository;
  private final LastReadMessageRepository lastReadMessageRepository;

  @Override
  public Long createChat(ChatRequestDTO dto) {
    Member owner = memberRepository.findById(dto.getOwner()).orElseThrow();
    Product product = productRepository.findById(dto.getProductId()).orElseThrow();
    Chat chat = Chat.builder()
            .message(dto.getMessage())
            .owner(owner)
            .productId(product)
            .build();

    Chat save = chatRepository.save(chat);
    return save.getId();
  }

  @Override
  public ChatroomResponseDTO findChatByProductId(Long productId) {
    return chatRepository.findChatByProductId(productId);
  }

  @Override
  public List<ChatroomResponseDTO> findAllChatByOwnerId(Long ownerId) {
    return chatRepository.findAllChatByOwnerId(ownerId);
  }

  @Override
  public List<ChatResponseDTO> findAllChatByProductId(Long productId) {
    return chatRepository.findAllChatByProductId(productId);
  }

  @Override
  public void markAsRead(Long productId, Long ownerId) {
    Long lastMessageId = lastReadMessageRepository.findLastMessageIdByProductId(productId);
    if (lastMessageId == null) {
      return;
    }

    LastReadMessage lastRead = lastReadMessageRepository
            .findByIdProductIdAndIdOwnerId(productId, ownerId)
            .orElse(new LastReadMessage(new LastReadMessageId(productId, ownerId), lastMessageId));

    lastRead.setLastReadId(lastMessageId);
    lastReadMessageRepository.save(lastRead);
  }

  @Override
  public ProductResponseDTO getProductByProductId(Long productId) {
    Product product = productRepository.findById(productId).orElseThrow();
    return new ProductResponseDTO(productId, product.getTitle(), product.getSeller().getId());
  }

  @Override
  public List<ProductResponseDTO> getAllProductBySellerId(Long sellerId) {
    List<Product> products = productRepository.findBySellerId(sellerId);
    List<ProductResponseDTO> productListResponseDTO = new ArrayList<>();
    for (Product p : products) {
      productListResponseDTO.add(new ProductResponseDTO(p.getId(), p.getTitle(), p.getSeller().getId()));
    }
    return productListResponseDTO;
  }

  @Override
  public int getTotalUnreadMessageCount(Long id) {
    List<ChatroomResponseDTO> allChatByOwnerId = findAllChatByOwnerId(id);
    return allChatByOwnerId.stream()
            .mapToInt(chatroom -> getUnreadMessageCount(chatroom.getProductId(), id))
            .sum();
  }


  public int getUnreadMessageCount(Long productId, Long ownerId) {
    LastReadMessage lastRead = lastReadMessageRepository
            .findByIdProductIdAndIdOwnerId(productId, ownerId)
            .orElse(new LastReadMessage(new LastReadMessageId(productId, ownerId), 0L));
    Long lastMessageId = lastReadMessageRepository.findLastMessageIdByProductId(productId);

    if (lastMessageId == null) {
      return 0;
    }

    return lastReadMessageRepository.countUnreadMessages(productId, lastRead.getLastReadId());
  }
}
