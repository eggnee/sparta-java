package com.sparta.task.service.dto;

import com.sparta.task.domain.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class SignUpRequest {

  private String username;
  private String password;

  public User toEntity(String encodedPassword) {
    return User.of(username, encodedPassword);
  }

}
