package org.threefour.ddip.chat.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.threefour.ddip.chat.domain.Chat;
import org.threefour.ddip.chat.domain.dto.ChatRequestDTO;
import org.threefour.ddip.chat.repository.ChatRepository;
import org.threefour.ddip.member.domain.Member;
import org.threefour.ddip.member.repository.MemberRepository;
import org.threefour.ddip.product.domain.Product;
import org.threefour.ddip.product.repository.ProductRepository;

@Service
@RequiredArgsConstructor
@Transactional
public class ChatServiceImpl implements ChatService {

  private final ChatRepository chatRepository;
  private final MemberRepository memberRepository;
  private final ProductRepository productRepository;

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
}
