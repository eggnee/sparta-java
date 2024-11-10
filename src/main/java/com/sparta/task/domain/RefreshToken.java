package com.sparta.task.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.Date;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RefreshToken {

  private static final long REFRESH_TOKEN_TIME = 24 * 60 * 60 * 1000L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  @Column(nullable = false)
  private Long userId;
  @Column(nullable = false)
  private String refreshToken;
  @Column(nullable = false)
  private Date expiration;

  private RefreshToken(Long userId, String refreshToken, Date expiration) {
    this.userId = userId;
    this.refreshToken = refreshToken;
    this.expiration = expiration;
  }

  public static RefreshToken createRefreshToken(Long userId) {
    Date issuedDate = new Date();
    Date expirationDate = new Date(issuedDate.getTime() + REFRESH_TOKEN_TIME);

    SecureRandom secureRandom = new SecureRandom();
    byte[] randomBytes = new byte[24];
    secureRandom.nextBytes(randomBytes);
    String refreshToken = Base64.getEncoder().encodeToString(randomBytes);

    return new RefreshToken(userId, refreshToken, expirationDate);
  }

  public void updateExpiration() {
    this.expiration = new Date(System.currentTimeMillis() + REFRESH_TOKEN_TIME);
  }

}
