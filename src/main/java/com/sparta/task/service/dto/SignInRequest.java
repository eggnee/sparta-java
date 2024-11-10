package com.sparta.task.service.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SignInRequest {

  @NotBlank(message = "사용자명은 필수값입니다.")
  private String username;
  @NotBlank(message = "비밀번호는 필수값입니다.")
  private String password;

}
