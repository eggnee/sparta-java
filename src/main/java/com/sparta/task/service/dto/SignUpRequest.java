package com.sparta.task.service.dto;

import com.sparta.task.domain.User;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class SignUpRequest {

  @NotBlank(message = "사용자명은 필수값입니다.")
  private String username;
  @NotBlank(message = "비밀번호는 필수값입니다.")
  private String password;
  @NotBlank(message = "닉네임은 필수값입니다.")
  private String nickname;

  public User toEntity(String encodedPassword) {
    return User.of(username, encodedPassword, nickname);
  }

}
