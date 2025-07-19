package com.alisondev.live_storage_hub.controller;

import com.alisondev.live_storage_hub.dto.*;
import com.alisondev.live_storage_hub.entity.User;
import com.alisondev.live_storage_hub.service.AuthService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {
  private final AuthService authService;

  public AuthController(AuthService authService) {
    this.authService = authService;
  }

  @PostMapping("/register")
  public ApiResponse<UserResponse> register(@RequestHeader("X-Api-Key") String apiKey,
      @RequestBody UserRegisterRequest request) {
    User user = authService.register(apiKey, request);
    return ApiResponse.ok(toDto(user));
  }

  @PostMapping("/login")
  public ApiResponse<AuthResponse> login(@RequestHeader("X-Api-Key") String apiKey,
      @RequestBody LoginRequest request) {
    return ApiResponse.ok(authService.login(apiKey, request));
  }

  private UserResponse toDto(User user) {
    UserResponse dto = new UserResponse();
    dto.setId(user.getId());
    dto.setName(user.getName());
    dto.setEmail(user.getEmail());
    dto.setCreatedAt(user.getCreatedAt());
    return dto;
  }
}
