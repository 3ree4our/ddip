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

  //TODO 중복된 데이터를 .. 가져옴 (채티방 중복..)
  @GetMapping("/{member}/chatrooms")
  public ResponseEntity<List<ChatroomResponseDTO>> getAllChatroom(@PathVariable("member") String email, @RequestHeader("Authorization") String token) {
    // 채팅방 갔을 때 응답해주는 컨트롤러
    // 채티방을 응답해준다.
    String accessToken = token.substring(7);
    Long id = jwtUtil.getId(accessToken);

    List<ChatroomResponseDTO> list = chatRepository.findAllChatByReceiverId(id);
    return new ResponseEntity<>(list, HttpStatus.OK);
  }


  /**
   * 첫 대화 신청, 메시지 보내는 것을 여기서 처리
   *
   * @param email
   * @param productId
   * @return
   */
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
