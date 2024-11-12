package com.sparta.task.util;

import com.sparta.task.service.AuthService;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestHeader;

@Slf4j
@ControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {

  private final AuthService authService;
  private final JwtUtil jwtUtil;

  @ExceptionHandler(ExpiredJwtException.class)
  public ResponseEntity<String> handleExpiredToken(ExpiredJwtException ex,
      @RequestHeader("Authorization") String accessToken) {
    try {
      Long userId = Long.valueOf((String) jwtUtil.extractClaims(accessToken).get("Id"));
      String newAccessToken = authService.refreshAccessToken(userId);
      return ResponseEntity.ok()
          .header(HttpHeaders.AUTHORIZATION, newAccessToken)
          .body("access token 이 갱신되었습니다.");
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
          .body("refresh token 이 유효하지 않습니다.");
    }
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<String> handleException(Exception ex) {
    log.error(ex.getMessage(), ex);
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
        .body("내부 서버 오류가 발생했습니다.");
  }
}
