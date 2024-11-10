package com.sparta.task.domain;

public record UserPrincipal(
    Long id,
    String username,
    String role
) {

  public static UserPrincipal of(Long id, String username, String role) {
    return new UserPrincipal(id, username, role);
  }

}
