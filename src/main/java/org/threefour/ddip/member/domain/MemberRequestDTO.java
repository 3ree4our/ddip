package org.threefour.ddip.member.domain;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Getter
@Setter
public class MemberRequestDTO {
  @NotBlank(message = "이메일은 필수 입력값입니다.")
  @Email
  private String email;

  @NotBlank(message = "비밀번호는 필수 입력값입니다.")
  @Pattern(regexp="(?=.*[0-9])(?=.*[a-zA-Z])(?=.*\\W)(?=\\S+$).{8,20}",
          message = "비밀번호는 영문 대,소문자와 숫자, 특수기호가 적어도 1개 이상씩 포함된 8자 ~ 20자의 비밀번호여야 합니다.")
  private String password;

  @NotBlank(message = "닉네임은 필수 입력값입니다.")
  @Pattern(regexp = "^[가-힣a-zA-Z0-9]{2,10}$" , message = "닉네임은 특수문자를 포함하지 않은 2~10자리여야 합니다.")
  private String nickName;

  @NotBlank(message = "학교는 필수 입력값입니다.")
  private String school;
}