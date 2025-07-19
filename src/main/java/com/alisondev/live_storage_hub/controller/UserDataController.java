package com.alisondev.live_storage_hub.controller;

import com.alisondev.live_storage_hub.entity.*;
import com.alisondev.live_storage_hub.repository.*;
import com.alisondev.live_storage_hub.security.JwtUtil;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/user_data")
public class UserDataController {
  private final AppRepository appRepository;
  private final UserRepository userRepository;
  private final UserDataRepository userDataRepository;
  private final JwtUtil jwtUtil;

  public UserDataController(AppRepository appRepository, UserRepository userRepository,
      UserDataRepository userDataRepository, JwtUtil jwtUtil) {
    this.appRepository = appRepository;
    this.userRepository = userRepository;
    this.userDataRepository = userDataRepository;
    this.jwtUtil = jwtUtil;
  }

  @PostMapping
  public UserData saveUserData(@RequestHeader("Authorization") String authHeader,
      @RequestBody Map<String, Object> payload) {
    String token = authHeader.substring(7);
    Long appId = jwtUtil.getAppIdFromToken(token);

    App app = appRepository.findById(appId).orElseThrow();
    Long userId = Long.valueOf(payload.get("userId").toString());

    User user = userRepository.findById(userId).orElseThrow();

    if (!user.getApp().getId().equals(appId)) {
      throw new RuntimeException("User does not registered in current application.");
    }

    UserData userData = UserData.builder()
        .app(app)
        .user(user)
        .jsonData((Map<String, Object>) payload.get("data"))
        .build();

    return userDataRepository.save(userData);
  }

  @GetMapping
  public List<UserData> listUserData(@RequestHeader("Authorization") String authHeader,
      @RequestParam Long userId) {
    String token = authHeader.substring(7);
    Long appId = jwtUtil.getAppIdFromToken(token);

    App app = appRepository.findById(appId).orElseThrow();
    User user = userRepository.findById(userId).orElseThrow();

    if (!user.getApp().getId().equals(appId)) {
      throw new RuntimeException("User does not registered in current application.");
    }

    return userDataRepository.findByAppAndUser(app, user);
  }
}
