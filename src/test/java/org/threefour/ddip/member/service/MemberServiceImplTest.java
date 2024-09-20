package org.threefour.ddip.member.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import org.threefour.ddip.member.domain.Member;
import org.threefour.ddip.member.domain.MemberRequestDTO;
import org.threefour.ddip.product.domain.Price;
import org.threefour.ddip.product.domain.Product;
import org.threefour.ddip.product.repository.ProductRepository;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class MemberServiceImplTest {

  @Autowired
  MemberServiceImpl memberService;

  @Test
  @DisplayName("회원가입")
  @Rollback(false)
  void addMember() {
    MemberRequestDTO memberRequestDTO1 = new MemberRequestDTO();
    memberRequestDTO1.setEmail("suwan");
    memberRequestDTO1.setPassword("suwan");
    memberRequestDTO1.setNickName("이수완");
    memberRequestDTO1.setSchool("보령중");

    memberService.join(memberRequestDTO1);

    MemberRequestDTO memberRequestDTO2 = new MemberRequestDTO();
    memberRequestDTO2.setEmail("duwan");
    memberRequestDTO2.setPassword("duwan");
    memberRequestDTO2.setNickName("이두완");
    memberRequestDTO2.setSchool("주포초");

    memberService.join(memberRequestDTO2);

    MemberRequestDTO memberRequestDTO3 = new MemberRequestDTO();
    memberRequestDTO3.setEmail("wuwan");
    memberRequestDTO3.setPassword("wuwan");
    memberRequestDTO3.setNickName("이우완");
    memberRequestDTO3.setSchool("시드니대");

    memberService.join(memberRequestDTO3);

  }
}