package org.threefour.ddip.member.service;

import org.threefour.ddip.member.domain.MemberRequestDTO;

public interface MemberService {
  Long join(MemberRequestDTO memberRequestDTO);
}