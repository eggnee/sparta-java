package com.sparta.task.util;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtUtil {

  public static final String BEARER_PREFIX = "Bearer ";
  public static final String ID = "id";
  public static final String USERNAME = "username";
  public static final String ROLE = "role";
  private final long TOKEN_TIME = 60 * 30 * 1000L;

  @Value("${jwt.secret-key}")
  private String secretKey;
  @Value("${spring.application.name}")
  private String issuer;
  private Key key;
  private final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

  // 바이트 배열로 변환 후 JWT 서명에 사용할 HMAC SHA 알고리즘용 키 초기화
  @PostConstruct
  public void init() {
    key = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
  }

  // 토큰 생성
  public String createToken(Long userId, String username, String roleDetails) {
    Date issuedDate = new Date();

    return BEARER_PREFIX +
        Jwts.builder()
            .issuer(issuer)
            .issuedAt(issuedDate)
            .claim(ID, userId.toString())
            .claim(USERNAME, username)
            .claim(ROLE, roleDetails)
            .expiration(new Date(issuedDate.getTime() + TOKEN_TIME))
            .signWith(key)  // HMAC-SHA256 알고리즘을 명시적으로 지정하지 않아도 됨
            .compact();
  }
}
