package org.threefour.ddip.chat.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import org.threefour.ddip.chat.domain.Chat;
import org.threefour.ddip.chat.domain.dto.ChatResponseDTO;
import org.threefour.ddip.chat.domain.dto.ChatroomResponseDTO;
import org.threefour.ddip.chat.domain.dto.ProductResponseDTO;
import org.threefour.ddip.chat.repository.ChatRepository;
import org.threefour.ddip.product.service.ProductServiceImpl;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class APIController {

  private final ChatRepository chatRepository;
  private final ProductServiceImpl productService;

  @GetMapping("/{member}/products")
  public ResponseEntity<List<ProductResponseDTO>> getAllProductByMemberId(@PathVariable("member") Long memberId, @RequestHeader("Authorization") String token) {
    // 아직은 회원 정보를 브라우저에 저장 못하니 임의로 하겠다.
    System.out.println("엄지야: "+token);
    List<ProductResponseDTO> list = productService.getAllProductBySellerId(memberId);
    return new ResponseEntity<>(list, HttpStatus.OK);
  }

  //TODO 중복된 데이터를 .. 가져옴 (채티방 중복..)
  @GetMapping("/{member}/chatrooms")
  public ResponseEntity<List<ChatroomResponseDTO>> getAllChatroom(@PathVariable("member") String email) {
    List<ChatroomResponseDTO> list = new ArrayList<>();
    return new ResponseEntity<>(list, HttpStatus.OK);
  }

  @GetMapping("/{member}/chatrooms/{productId}")
  public ResponseEntity<List<ChatResponseDTO>> getChatroomByProductId(@PathVariable("member") String email, @PathVariable("productId") Long productId) {
    List<ChatResponseDTO> list = new ArrayList<>();
    List<Chat> allChatByProductId = chatRepository.findAllChatByProductId(productId);
    for (Chat chat : allChatByProductId) {
      list.add(new ChatResponseDTO(chat));
    }

    return new ResponseEntity<>(list, HttpStatus.OK);
  }


}