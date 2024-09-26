package org.threefour.ddip.member.domain;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class MemberDetails implements UserDetails {
  private final Member member;

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return member.getRoles().stream()
            .map(role -> new SimpleGrantedAuthority(role.getRole().name()))
            .collect(Collectors.toList());
  }

  public Long getId() {
    return member.getId();
  }

  public String getNickName() {
    return member.getNickName();
  }

  @Override
  public String getUsername() {
    return member.getEmail();
  }

  @Override
  public String getPassword() {
    return member.getPassword();
  }

  public String getNickname(){
    return member.getNickName();
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return true;
  }
}