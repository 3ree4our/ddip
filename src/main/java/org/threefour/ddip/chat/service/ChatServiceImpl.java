package org.threefour.ddip.chat.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.threefour.ddip.chat.domain.Chat;
import org.threefour.ddip.chat.domain.dto.*;
import org.threefour.ddip.chat.repository.ChatRepository;
import org.threefour.ddip.chat.repository.LastReadMessageRepository;
import org.threefour.ddip.deal.domain.Deal;
import org.threefour.ddip.deal.domain.DealStatus;
import org.threefour.ddip.deal.repository.DealRepository;
import org.threefour.ddip.image.domain.Image;
import org.threefour.ddip.image.domain.TargetType;
import org.threefour.ddip.image.repository.ImageRepository;
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
  private final DealRepository dealRepository;
  private final ImageRepository imageRepository;

  @Override
  public Long createChat(ChatRequestDTO dto) {
    Member owner = memberRepository.findById(dto.getOwner()).orElseThrow();
    Product product = productRepository.findById(dto.getProductId()).orElseThrow();

    List<Deal> deals = dealRepository.findByProductIdAndAndDeleteYnFalse(product.getId());

    for (Deal deal : deals) {
      if (deal.getDealStatus() == DealStatus.PAID) throw new RuntimeException("Deal is paid");
    }

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
    List<ChatroomResponseDTO> allChats = chatRepository.findAllChatByOwnerIdOrSellerId(ownerId);
    System.out.println("이게 없는거잖아 " + allChats.size());
    List<ChatroomResponseDTO> chatListResponseDTO = new ArrayList<>();
    for (ChatroomResponseDTO chat : allChats) {
      if (chat.getProductId() != null) {  // productId가 null이 아닌 경우에만 이미지 조회
        List<Image> images = imageRepository.findByTargetTypeAndTargetIdAndDeleteYnFalse(TargetType.PRODUCT, chat.getProductId());
        if (!images.isEmpty()) {
          String s3Url = images.get(0).getS3Url();
          chat.setImage(s3Url);
        }
      }
      chatListResponseDTO.add(chat);
    }
    return chatListResponseDTO;
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
  public List<ProductResponseDTO> getAllProductBySellerId(Long sellerId) {
    List<Product> products = productRepository.findBySellerId(sellerId);
    List<ProductResponseDTO> productListResponseDTO = new ArrayList<>();

    for (Product p : products) {
      List<Image> byTargetTypeAndTargetIdAndDeleteYnFalse = imageRepository.findByTargetTypeAndTargetIdAndDeleteYnFalse(TargetType.PRODUCT, p.getId());
      String s3Url = byTargetTypeAndTargetIdAndDeleteYnFalse.get(0).getS3Url();

      productListResponseDTO.add(new ProductResponseDTO(p.getId(), p.getTitle(), p.getSeller().getId(), s3Url));
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

  public List<ChatroomResponseDTO> findAllActiveChatsForUser(Long userId) {
    List<Deal> activeDeals = dealRepository.findActiveDealsForUser(userId);
    List<ChatroomResponseDTO> result = new ArrayList<>();

    for (Deal deal : activeDeals) {
      ChatroomResponseDTO latestChat = chatRepository.findLatestChatByProductId(deal.getProduct().getId());
      if (latestChat != null) {
        latestChat.setStatus(deal.getDealStatus().name());
        List<Image> images = imageRepository.findByTargetTypeAndTargetIdAndDeleteYnFalse(TargetType.PRODUCT, latestChat.getProductId());
        if (!images.isEmpty()) {
          latestChat.setImage(images.get(0).getS3Url());
        }
        result.add(latestChat);
      }
    }

    return result;
  }
}
