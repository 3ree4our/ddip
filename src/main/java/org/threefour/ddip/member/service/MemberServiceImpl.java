package org.threefour.ddip.member.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.threefour.ddip.member.domain.Member;
import org.threefour.ddip.member.domain.MemberRequestDTO;
import org.threefour.ddip.member.repository.MemberRepository;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {
  private final MemberRepository memberRepository;
  private final BCryptPasswordEncoder bCryptPasswordEncoder;

  public Long join(MemberRequestDTO memberRequestDTO) {
    String email = memberRequestDTO.getEmail();
    String password = memberRequestDTO.getPassword();
    String nickName = memberRequestDTO.getNickName();
    String school = memberRequestDTO.getSchool();

    Boolean isExist = memberRepository.existsByEmail(email);
    if (isExist) {
      System.out.println("회원이 이미 존재합니다!");
      return 0L;
    }

    /*Member member = new Member();
    member.setNickName(nickName);
    member.setPassword(bCryptPasswordEncoder.encode(password));
    member.setSchool(school);
    member.setEmail(email);
*/
    Member member = Member.builder()
            .nickName(nickName)
            .password(bCryptPasswordEncoder.encode(password))
            .school(school)
            .email(email)
            .build();
    memberRepository.save(member);

    return member.getId();
  }
}