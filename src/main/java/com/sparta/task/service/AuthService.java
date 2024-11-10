package com.sparta.task.service;

import com.sparta.task.domain.User;
import com.sparta.task.domain.repository.UserRepository;
import com.sparta.task.service.dto.SignInRequest;
import com.sparta.task.service.dto.SignUpRequest;
import com.sparta.task.util.JwtUtil;
import jdk.jshell.spi.ExecutionControl.UserException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AuthService {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final JwtUtil jwtUtil;
  private final String MASTER_TOKEN = "MASTER_TOKEN";

  @Transactional
  public Long signUp(SignUpRequest signUpRequest) {
    validateDuplicateUsername(signUpRequest.getUsername());
    String encodedPassword = passwordEncoder.encode(signUpRequest.getPassword());
    User user = signUpRequest.toEntity(encodedPassword);
    return userRepository.save(user).getId();
  }


  public String signIn(SignInRequest signInRequest) {
    User verifiedUser = verifyUser(signInRequest);
    return jwtUtil.createToken(
        verifiedUser.getId(),
        verifiedUser.getUsername(),
        verifiedUser.getRole().toString()
    );
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

}
