package com.sparta.task.service;

import com.sparta.task.domain.RefreshToken;
import com.sparta.task.domain.User;
import com.sparta.task.domain.repository.RefreshTokenRepository;
import com.sparta.task.domain.repository.UserRepository;
import com.sparta.task.service.dto.SignInRequest;
import com.sparta.task.service.dto.SignInResponse;
import com.sparta.task.service.dto.SignUpRequest;
import com.sparta.task.service.dto.SignUpResponse;
import com.sparta.task.util.JwtUtil;
import java.util.Date;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AuthService {

  private final UserRepository userRepository;
  private final RefreshTokenRepository refreshTokenRepository;
  private final PasswordEncoder passwordEncoder;
  private final JwtUtil jwtUtil;
  private final String MASTER_TOKEN = "MASTER_TOKEN";

  @Transactional
  public SignUpResponse signUp(SignUpRequest signUpRequest) {
    validateDuplicateUsername(signUpRequest.getUsername());
    String encodedPassword = passwordEncoder.encode(signUpRequest.getPassword());
    User user = signUpRequest.toEntity(encodedPassword);
    return SignUpResponse.fromEntity(userRepository.save(user));
  }


  public SignInResponse signIn(SignInRequest signInRequest) {
    User verifiedUser = verifyUser(signInRequest);

    Optional<RefreshToken> existingRefreshToken = refreshTokenRepository.findByUserId(verifiedUser.getId());

    RefreshToken refreshToken;
    if (existingRefreshToken.isPresent()) {
      refreshToken = existingRefreshToken.get();
      refreshToken.updateExpiration();
      refreshTokenRepository.save(refreshToken);
    } else {
      refreshToken = RefreshToken.createRefreshToken(verifiedUser.getId());
      refreshTokenRepository.save(refreshToken);
    }

    return new SignInResponse(jwtUtil.createAccessToken(
        verifiedUser.getId(),
        verifiedUser.getUsername(),
        verifiedUser.getRole().toString()
    ));
  }

  private User verifyUser(SignInRequest signInRequest) {
    User user = userRepository.findByUsername(signInRequest.getUsername()).orElseThrow(
        () -> new IllegalArgumentException("사용자를 찾을 수 없습니다.")
    );
    checkPassword(user, signInRequest.getPassword());
    return user;

  }

  private void checkPassword(User user, String password) {
    if (!passwordEncoder.matches(password, user.getPassword())) {
      throw new IllegalArgumentException("비밀번호가 올바르지 않습니다.");
    }
  }
  private void validateDuplicateUsername(String username) {
    if (userRepository.existsByUsername(username)) {
      throw new IllegalArgumentException("중복된 사용자 이름입니다.");
    }
  }

  public String refreshAccessToken(Long userId) {
    RefreshToken existingRefreshToken = refreshTokenRepository.findByUserId(userId)
        .orElseThrow(() -> new RuntimeException("refresh token 이 존재하지 않습니다."));

    if (existingRefreshToken.getExpiration().before(new Date())) {
      throw new RuntimeException("refresh token 이 만료되었습니다.");
    }

    User user = userRepository.findById(existingRefreshToken.getUserId())
        .orElseThrow(() -> new RuntimeException("사용자가 존재하지 않습니다."));

    return jwtUtil.createAccessToken(
        user.getId(),
        user.getUsername(),
        user.getRole().toString()
    );
  }
}
