package com.alisondev.live_storage_hub.modules.users.services;

import com.alisondev.live_storage_hub.modules.apps.entities.App;
import com.alisondev.live_storage_hub.modules.users.entities.User;
import com.alisondev.live_storage_hub.modules.apps.repositories.AppRepository;
import com.alisondev.live_storage_hub.modules.users.repositories.UserRepository;
import com.alisondev.live_storage_hub.modules.users.dtos.AuthResponseDTO;
import com.alisondev.live_storage_hub.modules.users.dtos.LoginUserDTO;
import com.alisondev.live_storage_hub.modules.users.dtos.RegisterUserDTO;
import com.alisondev.live_storage_hub.modules.users.dtos.UserResponseDTO;
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

  public User register(String apiKey, RegisterUserDTO request) {
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

  public AuthResponseDTO login(String apiKey, LoginUserDTO request) {
    App app = appRepository.findByApiKey(apiKey)
        .orElseThrow(() -> new RuntimeException("App não encontrado"));

    User user = userRepository.findByAppAndEmail(app, request.getEmail())
        .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

    if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
      throw new RuntimeException("Senha inválida");
    }

    String token = jwtUtil.generateToken(user.getEmail(), app.getId());

    AuthResponseDTO response = new AuthResponseDTO();
    response.setToken(token);

    UserResponseDTO userResponse = new UserResponseDTO();
    userResponse.setId(user.getId());
    userResponse.setName(user.getName());
    userResponse.setEmail(user.getEmail());
    userResponse.setCreatedAt(user.getCreatedAt());
    response.setUser(userResponse);

    return response;
  }
}
