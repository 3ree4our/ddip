package org.threefour.ddip.member.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import org.threefour.ddip.member.domain.Member;
import org.threefour.ddip.member.domain.MemberRequestDTO;
import org.threefour.ddip.product.domain.Product;
import org.threefour.ddip.product.repository.ProductRepository;

@SpringBootTest
@Transactional
class MemberServiceImplTest {

  @Autowired
  MemberServiceImpl memberService;

  @Autowired
  ProductRepository productRepository;

  @Test
  @DisplayName("회원가입")
  @Rollback(false)
  void addMember() {
    MemberRequestDTO memberRequestDTO1 = new MemberRequestDTO();
    memberRequestDTO1.setEmail("suwan");
    memberRequestDTO1.setPassword("suwan");
    memberRequestDTO1.setNickName("이수완");
    memberRequestDTO1.setSchool("보령중");

    Long id1 = memberService.join(memberRequestDTO1);

    MemberRequestDTO memberRequestDTO2 = new MemberRequestDTO();
    memberRequestDTO2.setEmail("duwan");
    memberRequestDTO2.setPassword("duwan");
    memberRequestDTO2.setNickName("이두완");
    memberRequestDTO2.setSchool("주포초");

    Long id2 = memberService.join(memberRequestDTO2);

    MemberRequestDTO memberRequestDTO3 = new MemberRequestDTO();
    memberRequestDTO3.setEmail("wuwan");
    memberRequestDTO3.setPassword("wuwan");
    memberRequestDTO3.setNickName("이우완");
    memberRequestDTO3.setSchool("시드니대");

    Long id3 = memberService.join(memberRequestDTO3);

    Product product1 = Product.builder()
            .title("갤럭시!!")
            .content("갤럭시팔아요")
            .name("이수완")
            .price(39000)
            .seller(Member.builder()
                    .id(id1)
                    .build())
            .build();
    Product product2 = Product.builder()
            .title("아이폰!!")
            .content("아이폰팔아요")
            .name("이수완")
            .price(49000)
            .seller(Member.builder()
                    .id(id2)
                    .build())
            .build();
    Product product3 = Product.builder()
            .title("wuwan폰!!!!")
            .content("뭐요팔아요")
            .name("이우완")
            .price(59000)
            .seller(Member.builder()
                    .id(id3)
                    .build())
            .build();
    productRepository.save(product1);
    productRepository.save(product2);
    productRepository.save(product3);

  }
}