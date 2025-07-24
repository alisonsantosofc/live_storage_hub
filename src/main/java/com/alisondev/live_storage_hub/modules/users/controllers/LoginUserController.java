package com.alisondev.live_storage_hub.modules.users.controllers;

import com.alisondev.live_storage_hub.dtos.CustomApiResponse;
import com.alisondev.live_storage_hub.modules.users.dtos.AuthResponseDTO;
import com.alisondev.live_storage_hub.modules.users.dtos.LoginUserDTO;
import com.alisondev.live_storage_hub.modules.users.services.LoginUserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/sessions")
@Tag(name = "Authentication", description = "Endpoints para autenticação.")
public class LoginUserController {
  private final LoginUserService loginUserService;

  public LoginUserController(LoginUserService loginUserService) {
    this.loginUserService = loginUserService;
  }

  @Operation(summary = "Login do usuário", description = "Realiza autenticação e retorna JWT.")
  @PostMapping("/login")
  public CustomApiResponse<AuthResponseDTO> handle(@RequestHeader("X-Api-Key") String apiKey,
      @RequestBody LoginUserDTO request) {
    return CustomApiResponse.ok(loginUserService.execute(apiKey, request));
  }
}
