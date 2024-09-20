package org.threefour.ddip.member.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.threefour.ddip.address.domain.Address;
import org.threefour.ddip.address.service.AddressService;
import org.threefour.ddip.member.domain.Member;
import org.threefour.ddip.member.domain.MemberRequestDTO;
import org.threefour.ddip.member.repository.MemberRepository;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {
  private final AddressService addressService;
  private final MemberRepository memberRepository;
  private final BCryptPasswordEncoder bCryptPasswordEncoder;


  @Override
  public Member saveMember(Member member, Address address){
    memberRepository.save(member);
    addressService.saveAddress(address);
    return member;
  }

  public Boolean join(MemberRequestDTO memberRequestDTO){
    String email = memberRequestDTO.getEmail();
    String password = memberRequestDTO.getPassword();
    String nickname = memberRequestDTO.getNickName();
    String school = memberRequestDTO.getSchool();

    Boolean isExist = memberRepository.existsByEmail(email);
    if(isExist){
      System.out.println("회원이 이미 존재합니다!");
      return false;
    }

    Member member = new Member();
    member.setNickName(nickname);
    member.setPassword(bCryptPasswordEncoder.encode(password));
    member.setSchool(school);
    member.setEmail(email);

    memberRepository.save(member);

    return true;
  }
}
