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
import org.threefour.ddip.member.jwt.JWTUtil;
import org.threefour.ddip.product.service.ProductServiceImpl;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class APIController {

  private final ChatRepository chatRepository;
  private final ProductServiceImpl productService;
  private final JWTUtil jwtUtil;

  @GetMapping("/{member}/products")
  public ResponseEntity<List<ProductResponseDTO>> getAllProductByMemberId(@PathVariable("member") String email, @RequestHeader("Authorization") String token) {
    // 아직은 회원 정보를 브라우저에 저장 못하니 임의로 하겠다.
    String accessToken = token.substring(7);
    Long id = jwtUtil.getId(accessToken);
    List<ProductResponseDTO> list = productService.getAllProductBySellerId(id);
    return new ResponseEntity<>(list, HttpStatus.OK);
  }

  @GetMapping("/{member}/chatrooms")
  public ResponseEntity<List<ChatroomResponseDTO>> getAllChatroom(@PathVariable("member") String email, @RequestHeader("Authorization") String token) {
    String accessToken = token.substring(7);
    Long id = jwtUtil.getId(accessToken);

    List<ChatroomResponseDTO> list = new ArrayList<>();

    List<Chat> chatList = chatRepository.findAllChatByReceiverId(id);
    for (Chat chat : chatList) {
      list.add(new ChatroomResponseDTO(chat));
    }
    return new ResponseEntity<>(list, HttpStatus.OK);
  }

  @GetMapping("/{member}/chatrooms/{chatroomId}")
  public ResponseEntity<List<ChatResponseDTO>> getChatroomByProductId(@PathVariable("member") String email, @PathVariable("chatroomId") Long chatroomId) {
    List<ChatResponseDTO> list = new ArrayList<>();
    List<Chat> allChatByProductId = chatRepository.findAllChatByProductId(chatroomId);
    for (Chat chat : allChatByProductId) {
      list.add(new ChatResponseDTO(chat));
    }

    return new ResponseEntity<>(list, HttpStatus.OK);
  }


}