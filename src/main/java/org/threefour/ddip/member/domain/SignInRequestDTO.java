package org.threefour.ddip.member.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignInRequestDTO {
  private String username;
  private String password;
}