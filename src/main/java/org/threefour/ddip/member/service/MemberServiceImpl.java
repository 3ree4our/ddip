package org.threefour.ddip.member.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.threefour.ddip.address.Repository.AddressRepository;
import org.threefour.ddip.address.domain.Address;
import org.threefour.ddip.address.service.AddressService;
import org.threefour.ddip.member.domain.Member;
import org.threefour.ddip.member.domain.MemberRequestDTO;
import org.threefour.ddip.member.domain.Role;
import org.threefour.ddip.member.repository.MemberRepository;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {
  private final AddressService addressService;
  private final MemberRepository memberRepository;
  private final BCryptPasswordEncoder bCryptPasswordEncoder;

  public Member saveMember(Member member, Address address) {
    memberRepository.save(member);
    addressService.saveAddress(address);
    return member;
  }

  public Boolean join(MemberRequestDTO memberRequestDTO, Address address) {
    String email = memberRequestDTO.getEmail();
    Boolean isExist = memberRepository.existsByEmail(email);

    if (isExist) {
      System.out.println("회원이 이미 존재합니다!");
      return false;
    }

    Member member = new Member();
    member.setNickName(memberRequestDTO.getNickName());
    member.setPassword(bCryptPasswordEncoder.encode(memberRequestDTO.getPassword()));
    member.setSchool(memberRequestDTO.getSchool());
    member.setEmail(email);
    member.setAddresses(List.of(address));

    Role userRole = memberRepository.findByRoles(Role.RoleType.ROLE_USER);
    member.getRoles().add(userRole);

    member = memberRepository.save(member);
    address.setLat(address.getLat());
    address.setLnt(address.getLnt());
    address.setMember(member);
    addressService.saveAddress(address);

    return true;
  }
}