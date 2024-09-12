package org.threefour.ddip.member.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.threefour.ddip.address.domain.Address;
import org.threefour.ddip.address.service.AddressService;
import org.threefour.ddip.member.domain.Member;
import org.threefour.ddip.member.repository.MemberRepository;

@Service
@Transactional
public class MemberServiceImpl implements MemberService {
  @Autowired
  private AddressService addressService;

  @Autowired
  private MemberRepository memberRepository;

  @Override
  public Member saveMember(Member member, Address address){
    memberRepository.save(member);
    addressService.saveAddress(address);

    return member;
  }
}
