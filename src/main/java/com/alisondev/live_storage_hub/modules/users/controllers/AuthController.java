package com.alisondev.live_storage_hub.modules.users.controllers;

import com.alisondev.live_storage_hub.dtos.*;
import com.alisondev.live_storage_hub.dtos.auth.AuthResponse;
import com.alisondev.live_storage_hub.dtos.auth.LoginRequest;
import com.alisondev.live_storage_hub.dtos.user.UserRegisterRequest;
import com.alisondev.live_storage_hub.dtos.user.UserResponse;
import com.alisondev.live_storage_hub.modules.users.entities.User;
import com.alisondev.live_storage_hub.modules.users.services.AuthService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@Tag(name = "Authentication", description = "Endpoints para autenticação.")
public class AuthController {
  private final AuthService authService;

  public AuthController(AuthService authService) {
    this.authService = authService;
  }

  @Operation(summary = "Registro de usuário", description = "Realiza registro de usuário e retorna dados do usuário.")
  @PostMapping("/register")
  public CustomApiResponse<UserResponse> register(@RequestHeader("X-Api-Key") String apiKey,
      @RequestBody UserRegisterRequest request) {
    User user = authService.register(apiKey, request);
    return CustomApiResponse.ok(toDto(user));
  }

  @Operation(summary = "Login do usuário", description = "Realiza autenticação e retorna JWT.")
  @PostMapping("/login")
  public CustomApiResponse<AuthResponse> login(@RequestHeader("X-Api-Key") String apiKey,
      @RequestBody LoginRequest request) {
    return CustomApiResponse.ok(authService.login(apiKey, request));
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
