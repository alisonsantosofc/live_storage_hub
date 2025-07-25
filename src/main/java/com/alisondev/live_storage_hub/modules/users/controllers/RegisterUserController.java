package com.alisondev.live_storage_hub.modules.users.controllers;

import com.alisondev.live_storage_hub.dtos.ApiResponse;
import com.alisondev.live_storage_hub.modules.users.dtos.RegisterUserDTO;
import com.alisondev.live_storage_hub.modules.users.dtos.UserResponseDTO;
import com.alisondev.live_storage_hub.modules.users.entities.User;
import com.alisondev.live_storage_hub.modules.users.services.RegisterUserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@Tag(name = "Users", description = "Endpoints for managing users.")
public class RegisterUserController {
  private final RegisterUserService registerUserService;

  public RegisterUserController(RegisterUserService registerUserService) {
    this.registerUserService = registerUserService;
  }

  @Operation(summary = "Registro de usuário", description = "Registers new user and return user info.")
  @PostMapping("/register")
  public ApiResponse<UserResponseDTO> handle(@RequestHeader("X-Api-Key") String apiKey,
      @RequestBody RegisterUserDTO request) {
    User user = registerUserService.execute(apiKey, request);
    return ApiResponse.ok(toDto(user));
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
