package com.sparta.task.service.dto;

import com.sparta.task.domain.User;
import java.util.HashMap;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SignUpResponse {

  private String username;
  private String nickname;
  private Map<String, String> authorities = new HashMap<>();

  public static SignUpResponse fromEntity(User user) {
    Map<String, String> authorities = new HashMap<>();
    authorities.put("authorityName", user.getRole().toString());
    return new SignUpResponse(user.getUsername(), user.getNickname(), authorities);
  }
}
