package com.sparta.task.filter;

import static com.sparta.task.util.JwtUtil.BEARER_PREFIX;

import com.sparta.task.domain.UserPrincipal;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import javax.crypto.SecretKey;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

@Slf4j
@Component
public class JwtFilter extends OncePerRequestFilter {

  @Value("${jwt.secret-key}")
  private String secretKey;

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
      FilterChain filterChain) throws ServletException, IOException {

    String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

    if (StringUtils.hasText(authorizationHeader)) {
      try {
        String token = extractToken(authorizationHeader);
        Claims claims = validateToken(token);

        String id = (String) claims.get("id");
        String username = (String) claims.get("username");
        String role = (String) claims.get("role");

        UserPrincipal userPrincipal =
            UserPrincipal.of(
                Long.valueOf(id),
                username,
                role
            );

        GrantedAuthority authority = new SimpleGrantedAuthority(role);
        Authentication authentication = new UsernamePasswordAuthenticationToken(
            userPrincipal, null, List.of(authority)
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
      } catch (Exception e) {
        log.error("Authentication 실패: {}", e.getMessage());
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        return;
      }
    }
    filterChain.doFilter(request, response);
  }

  // JWT 토큰 추출
  private String extractToken(String authorizationHeader) {
    if (!authorizationHeader.startsWith(BEARER_PREFIX)) {
      throw new IllegalArgumentException("올바르지 않은 토큰 값입니다.");
    }
    return authorizationHeader.substring(BEARER_PREFIX.length());
  }

  // 토큰 유효성 검사
  private Claims validateToken(String token) {
    SecretKey key = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
    try {
      return Jwts.parser()
          .verifyWith(key)
          .build()
          .parseSignedClaims(token)
          .getPayload();
    } catch (ExpiredJwtException e) {
      throw new RuntimeException("Expired token: " + e.getMessage());
    } catch (Exception unexpectedException) {
      throw new RuntimeException(unexpectedException);
    }
  }

  private static final List<String> EXCLUDE_URLS = List.of(
      "/api/auth/sign-in",
      "/api/auth/sign-up",
      "/h2-console"
  );

  @Override
  protected boolean shouldNotFilter(HttpServletRequest request) {
    return EXCLUDE_URLS.stream().anyMatch(url -> request.getRequestURI().startsWith(url));
  }
}
