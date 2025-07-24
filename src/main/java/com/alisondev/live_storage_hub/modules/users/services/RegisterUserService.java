package com.alisondev.live_storage_hub.modules.users.services;

import com.alisondev.live_storage_hub.modules.apps.entities.App;
import com.alisondev.live_storage_hub.modules.users.entities.User;
import com.alisondev.live_storage_hub.modules.apps.repositories.AppRepository;
import com.alisondev.live_storage_hub.modules.users.repositories.UserRepository;
import com.alisondev.live_storage_hub.modules.users.dtos.RegisterUserDTO;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class RegisterUserService {
  private final AppRepository appRepository;
  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;

  public RegisterUserService(AppRepository appRepository,
      UserRepository userRepository,
      PasswordEncoder passwordEncoder) {
    this.appRepository = appRepository;
    this.userRepository = userRepository;
    this.passwordEncoder = passwordEncoder;
  }

  public User execute(String apiKey, RegisterUserDTO request) {
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
}
