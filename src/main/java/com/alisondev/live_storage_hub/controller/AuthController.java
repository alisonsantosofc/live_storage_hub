package com.alisondev.live_storage_hub.controller;

import com.alisondev.live_storage_hub.entity.App;
import com.alisondev.live_storage_hub.entity.User;
import com.alisondev.live_storage_hub.repository.AppRepository;
import com.alisondev.live_storage_hub.repository.UserRepository;
import com.alisondev.live_storage_hub.security.JwtUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {
  private final AppRepository appRepository;
  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final JwtUtil jwtUtil;

  public AuthController(AppRepository appRepository,
      UserRepository userRepository,
      PasswordEncoder passwordEncoder,
      JwtUtil jwtUtil) {
    this.appRepository = appRepository;
    this.userRepository = userRepository;
    this.passwordEncoder = passwordEncoder;
    this.jwtUtil = jwtUtil;
  }

  @PostMapping("/register")
  public User register(@RequestBody Map<String, String> request) {
    String apiKey = request.get("apiKey");
    String email = request.get("email");
    String name = request.get("name");
    String password = request.get("password");

    App app = appRepository.findByApiKey(apiKey)
        .orElseThrow(() -> new RuntimeException("Invalid api key or app none exists."));

    if (userRepository.existsByAppAndEmail(app, email)) {
      throw new RuntimeException("User already registered in current app.");
    }

    User user = User.builder()
        .app(app)
        .name(name)
        .email(email)
        .password(passwordEncoder.encode(password))
        .build();

    return userRepository.save(user);
  }

  @PostMapping("/login")
  public Map<String, String> login(@RequestBody Map<String, String> credentials) {
    String apiKey = credentials.get("apiKey");
    String email = credentials.get("email");
    String password = credentials.get("password");

    App app = appRepository.findByApiKey(apiKey)
        .orElseThrow(() -> new RuntimeException("Invalid api key or app none exists."));

    User user = userRepository.findByAppAndEmail(app, email)
        .orElseThrow(() -> new RuntimeException("User does not exists."));

    if (!passwordEncoder.matches(password, user.getPassword())) {
      throw new RuntimeException("Invalid email or password.");
    }

    String token = jwtUtil.generateToken(email, app.getId());
    return Map.of("token", token);
  }
}
