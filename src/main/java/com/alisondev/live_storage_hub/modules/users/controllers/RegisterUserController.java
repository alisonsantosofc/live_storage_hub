package com.alisondev.live_storage_hub.modules.users.controllers;

import com.alisondev.live_storage_hub.dtos.SendApiResponse;
import com.alisondev.live_storage_hub.modules.users.dtos.RegisterUserDTO;
import com.alisondev.live_storage_hub.modules.users.dtos.RegisterUserResponseDTO;
import com.alisondev.live_storage_hub.modules.users.entities.User;
import com.alisondev.live_storage_hub.modules.users.services.RegisterUserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@Tag(name = "Users", description = "Endpoints for users.")
public class RegisterUserController {
  private final RegisterUserService registerUserService;

  public RegisterUserController(RegisterUserService registerUserService) {
    this.registerUserService = registerUserService;
  }

  @Operation(summary = "Register user", description = "Registers new user and return user info.")
  @PostMapping
  public SendApiResponse<RegisterUserResponseDTO> handle(@RequestHeader("X-Api-Key") String apiKey,
      @RequestBody RegisterUserDTO request) {
    User user = registerUserService.execute(apiKey, request);
    return SendApiResponse.ok(toDto(user));
  }

  private RegisterUserResponseDTO toDto(User user) {
    RegisterUserResponseDTO dto = new RegisterUserResponseDTO();
    dto.setId(user.getId());
    return dto;
  }
}
