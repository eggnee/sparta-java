package com.sparta.task;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.sparta.task.domain.User;
import com.sparta.task.domain.repository.UserRepository;
import com.sparta.task.mock.WithCustomMockUser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest
@AutoConfigureMockMvc
public class AuthControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private PasswordEncoder passwordEncoder;

  @BeforeEach
  void setUp() {
    String encodedPassword = passwordEncoder.encode("비밀번호");
    User user = User.of("username", encodedPassword, "아린");
    userRepository.save(user);
  }

  @Test
  public void testLoginSuccess() throws Exception {
    mockMvc.perform(post("/api/auth/sign-in")
            .contentType("application/json")
            .content("{ \"username\": \"username\", \"password\": \"비밀번호\" }"))
        .andExpect(status().isOk())
        .andExpect(header().exists("Authorization"))
        .andExpect(jsonPath("$.token").exists());
  }

  // JUnit 테스트 작성
  @Test
  @WithCustomMockUser
  public void testAuthentication() throws Exception {
    mockMvc.perform(get("/api/test")
            .header("X-AUTH-TOKEN", "aaaaaaa"))
        .andExpect(MockMvcResultMatchers.status().isOk());
  }



}
