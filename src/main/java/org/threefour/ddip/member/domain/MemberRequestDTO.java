package org.threefour.ddip.member.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MemberRequestDTO {
  private String email;
  private String password;
  private String nickName;
  private String school;
}