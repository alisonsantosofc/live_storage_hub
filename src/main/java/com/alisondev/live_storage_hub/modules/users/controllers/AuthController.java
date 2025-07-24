package com.alisondev.live_storage_hub.modules.users.controllers;

import com.alisondev.live_storage_hub.dtos.CustomApiResponse;
import com.alisondev.live_storage_hub.modules.users.dtos.AuthResponseDTO;
import com.alisondev.live_storage_hub.modules.users.dtos.LoginUserDTO;
import com.alisondev.live_storage_hub.modules.users.dtos.RegisterUserDTO;
import com.alisondev.live_storage_hub.modules.users.dtos.UserResponseDTO;
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
  public CustomApiResponse<UserResponseDTO> register(@RequestHeader("X-Api-Key") String apiKey,
      @RequestBody RegisterUserDTO request) {
    User user = authService.register(apiKey, request);
    return CustomApiResponse.ok(toDto(user));
  }

  @Operation(summary = "Login do usuário", description = "Realiza autenticação e retorna JWT.")
  @PostMapping("/login")
  public CustomApiResponse<AuthResponseDTO> login(@RequestHeader("X-Api-Key") String apiKey,
      @RequestBody LoginUserDTO request) {
    return CustomApiResponse.ok(authService.login(apiKey, request));
  }

  private UserResponseDTO toDto(User user) {
    UserResponseDTO dto = new UserResponseDTO();
    dto.setId(user.getId());
    dto.setName(user.getName());
    dto.setEmail(user.getEmail());
    dto.setCreatedAt(user.getCreatedAt());
    return dto;
  }
}
