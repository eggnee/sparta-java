package com.sparta.task.domain;

import com.sparta.task.domain.vo.Role;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "P_USER")
public class User {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  @Column(nullable = false, unique = true)
  private String username;
  @Column(nullable = false)
  private String password;
  @Column(nullable = false, unique = true)
  private String nickname;
  @Column(nullable = false)
  @Enumerated(EnumType.STRING)
  private Role role = Role.ROLE_USER;

  private User(String username, String password, String nickname) {
    this.username = username;
    this.password = password;
    this.nickname = nickname;
  }

  public static User of(String username, String encodedPassword, String nickname) {
    return new User(username, encodedPassword, nickname);
  }

}
