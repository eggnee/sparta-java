package com.sparta.task.mock;

import com.sparta.task.domain.UserPrincipal;
import java.util.List;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

public class WithCustomMockUserSecurityContextFactory implements
    WithSecurityContextFactory<WithCustomMockUser> {

  @Override
  public SecurityContext createSecurityContext(WithCustomMockUser annotation) {
    UserPrincipal userPrincipal = UserPrincipal.of(
        annotation.id(),
        annotation.username(),
        "ROLE_USER"
    );

    GrantedAuthority authority = new SimpleGrantedAuthority("ROLE_USER");

    Authentication auth = new UsernamePasswordAuthenticationToken(
        userPrincipal,
        null,
        List.of(authority)
    );

    SecurityContext context = SecurityContextHolder.getContext();
    context.setAuthentication(auth);

    return context;
  }
}
