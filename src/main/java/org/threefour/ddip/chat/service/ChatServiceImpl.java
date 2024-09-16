package org.threefour.ddip.chat.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.threefour.ddip.chat.domain.Chat;
import org.threefour.ddip.chat.domain.dto.ChatRequestDTO;
import org.threefour.ddip.chat.domain.dto.LastReadMessage;
import org.threefour.ddip.chat.domain.dto.LastReadMessageId;
import org.threefour.ddip.chat.repository.ChatRepository;
import org.threefour.ddip.chat.repository.LastReadMessageRepository;
import org.threefour.ddip.member.domain.Member;
import org.threefour.ddip.member.repository.MemberRepository;
import org.threefour.ddip.product.domain.Product;
import org.threefour.ddip.product.repository.ProductRepository;

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
  public Chat createChat(ChatRequestDTO dto) {
    Member owner = memberRepository.findById(dto.getOwner()).orElseThrow();
    Product product = productRepository.findById(dto.getProductId()).orElseThrow();
    Chat chat = Chat.builder()
            .message(dto.getMessage())
            .owner(owner)
            .productId(product)
            .build();

    chatRepository.save(chat);
    return null;
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

}
