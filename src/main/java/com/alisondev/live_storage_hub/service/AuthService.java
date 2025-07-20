package com.alisondev.live_storage_hub.service;

import com.alisondev.live_storage_hub.dto.*;
import com.alisondev.live_storage_hub.dto.user.UserRegisterRequest;
import com.alisondev.live_storage_hub.dto.user.UserResponse;
import com.alisondev.live_storage_hub.entity.App;
import com.alisondev.live_storage_hub.entity.User;
import com.alisondev.live_storage_hub.repository.AppRepository;
import com.alisondev.live_storage_hub.repository.UserRepository;
import com.alisondev.live_storage_hub.security.JwtUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

  private final AppRepository appRepository;
  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final JwtUtil jwtUtil;

  public AuthService(AppRepository appRepository,
      UserRepository userRepository,
      PasswordEncoder passwordEncoder,
      JwtUtil jwtUtil) {
    this.appRepository = appRepository;
    this.userRepository = userRepository;
    this.passwordEncoder = passwordEncoder;
    this.jwtUtil = jwtUtil;
  }

  public User register(String apiKey, UserRegisterRequest request) {
    App app = appRepository.findByApiKey(apiKey)
        .orElseThrow(() -> new RuntimeException("App não encontrado"));

    if (userRepository.findByAppAndEmail(app, request.getEmail()).isPresent()) {
      throw new RuntimeException("Usuário já existe para este App");
    }

    User user = User.builder()
        .app(app)
        .name(request.getName())
        .email(request.getEmail())
        .password(passwordEncoder.encode(request.getPassword()))
        .build();

    return userRepository.save(user);
  }

  public AuthResponse login(String apiKey, LoginRequest request) {
    App app = appRepository.findByApiKey(apiKey)
        .orElseThrow(() -> new RuntimeException("App não encontrado"));

    User user = userRepository.findByAppAndEmail(app, request.getEmail())
        .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

    if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
      throw new RuntimeException("Senha inválida");
    }

    // ✅ Agora passando email e appId para gerar o token
    String token = jwtUtil.generateToken(user.getEmail(), app.getId());

    AuthResponse response = new AuthResponse();
    response.setToken(token);

    UserResponse userResponse = new UserResponse();
    userResponse.setId(user.getId());
    userResponse.setName(user.getName());
    userResponse.setEmail(user.getEmail());
    userResponse.setCreatedAt(user.getCreatedAt());
    response.setUser(userResponse);

    return response;
  }
}
