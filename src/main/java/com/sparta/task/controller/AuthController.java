package com.sparta.task.controller;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

import com.sparta.task.service.AuthService;
import com.sparta.task.service.dto.SignInRequest;
import com.sparta.task.service.dto.SignUpRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

  private final AuthService authService;

  @PostMapping("/sign-up")
  public ResponseEntity<Long> signUp(@RequestBody SignUpRequest signUpRequest) {
    return ResponseEntity.ok(authService.signUp(signUpRequest));
  }

  @PostMapping("/sign-in")
  public ResponseEntity<String> signIn(
      @RequestBody SignInRequest signInRequest,
      HttpServletResponse response
  ) {
    response.setHeader(AUTHORIZATION, authService.signIn(signInRequest));
    return ResponseEntity.ok(authService.signIn(signInRequest));
  }
}
