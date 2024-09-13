package org.threefour.ddip.member.service;

import org.threefour.ddip.address.domain.Address;
import org.threefour.ddip.member.domain.Member;
import org.threefour.ddip.member.domain.MemberRequestDTO;

public interface MemberService {
  Member saveMember(Member member, Address address);

  Boolean join(MemberRequestDTO memberRequestDTO);

}
