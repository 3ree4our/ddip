package org.threefour.ddip.member.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.threefour.ddip.member.domain.Member;
import org.threefour.ddip.member.domain.MemberDetails;
import org.threefour.ddip.member.repository.MemberRepository;

@Service
@RequiredArgsConstructor
public class MemberDetailsService implements UserDetailsService {
  private final MemberRepository memberRepository;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    Member data = memberRepository.findByEmail(username);
    if (data != null) {
      return new MemberDetails(data);
    }
    return null;
  }
}