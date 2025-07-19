package com.alisondev.live_storage_hub.security;

import com.alisondev.live_storage_hub.entity.App;
import com.alisondev.live_storage_hub.entity.User;
import com.alisondev.live_storage_hub.repository.AppRepository;
import com.alisondev.live_storage_hub.repository.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
  private final JwtUtil jwtUtil;
  private final AppRepository appRepository;
  private final UserRepository userRepository;

  public JwtAuthenticationFilter(
      JwtUtil jwtUtil,
      AppRepository appRepository,
      UserRepository userRepository) {
    this.jwtUtil = jwtUtil;
    this.appRepository = appRepository;
    this.userRepository = userRepository;
  }

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {

    final String authHeader = request.getHeader("Authorization");

    if (authHeader != null && authHeader.startsWith("Bearer ")) {
      String jwt = authHeader.substring(7);
      if (jwtUtil.validateToken(jwt)) {
        String email = jwtUtil.getEmailFromToken(jwt);
        Long appId = jwtUtil.getAppIdFromToken(jwt);

        App app = appRepository.findById(appId).orElse(null);
        User user = (app != null) ? userRepository.findByAppAndEmail(app, email).orElse(null) : null;

        if (user != null && SecurityContextHolder.getContext().getAuthentication() == null) {
          var authToken = new UsernamePasswordAuthenticationToken(user, null, null);
          authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
          SecurityContextHolder.getContext().setAuthentication(authToken);
        }
      }
    }

    filterChain.doFilter(request, response);
  }
}
