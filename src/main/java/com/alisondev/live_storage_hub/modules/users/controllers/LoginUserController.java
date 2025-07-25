package com.alisondev.live_storage_hub.modules.users.controllers;

import com.alisondev.live_storage_hub.dtos.ApiResponse;
import com.alisondev.live_storage_hub.modules.users.dtos.AuthResponseDTO;
import com.alisondev.live_storage_hub.modules.users.dtos.LoginUserDTO;
import com.alisondev.live_storage_hub.modules.users.services.LoginUserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/sessions")
@Tag(name = "Authentication", description = "Endpoints for users authentication.")
public class LoginUserController {
  private final LoginUserService loginUserService;

  public LoginUserController(LoginUserService loginUserService) {
    this.loginUserService = loginUserService;
  }

  @Operation(summary = "Login user", description = "Authenticate user and return token.")
  @PostMapping("/login")
  public ApiResponse<AuthResponseDTO> handle(@RequestHeader("X-Api-Key") String apiKey,
      @RequestBody LoginUserDTO request) {
    return ApiResponse.ok(loginUserService.execute(apiKey, request));
  }
}
