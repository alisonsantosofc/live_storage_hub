package com.alisondev.live_storage_hub.modules.users.services;

import com.alisondev.live_storage_hub.modules.apps.entities.App;
import com.alisondev.live_storage_hub.modules.users.entities.User;
import com.alisondev.live_storage_hub.modules.apps.repositories.AppRepository;
import com.alisondev.live_storage_hub.modules.users.repositories.UserRepository;
import com.alisondev.live_storage_hub.modules.users.dtos.RegisterUserDTO;
import com.alisondev.live_storage_hub.modules.users.errors.UsersErrorPrefix;
import com.alisondev.live_storage_hub.exceptions.ApiRuntimeException;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class RegisterUserService {
  private final AppRepository appRepository;
  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final String prefix = UsersErrorPrefix.MODULE + "." + UsersErrorPrefix.ROUTE_REGISTER_USER + ".";

  public RegisterUserService(AppRepository appRepository,
      UserRepository userRepository,
      PasswordEncoder passwordEncoder) {
    this.appRepository = appRepository;
    this.userRepository = userRepository;
    this.passwordEncoder = passwordEncoder;
  }

  public User execute(String apiKey, RegisterUserDTO request) {
    App app = appRepository.findByApiKey(apiKey)
        .orElseThrow(() -> new ApiRuntimeException(prefix + 1, "App not found or invalid api key."));

    if (userRepository.findByAppAndEmail(app, request.getEmail()).isPresent()) {
      throw new ApiRuntimeException(prefix + 2, "User already registered for this app.");
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
