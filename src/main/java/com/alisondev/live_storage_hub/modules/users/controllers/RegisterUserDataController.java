package com.alisondev.live_storage_hub.modules.users.controllers;

import com.alisondev.live_storage_hub.dtos.CustomApiResponse;
import com.alisondev.live_storage_hub.modules.users.dtos.RegisterUserDataDTO;
import com.alisondev.live_storage_hub.modules.users.dtos.UserDataResponseDTO;
import com.alisondev.live_storage_hub.modules.users.entities.UserData;
import com.alisondev.live_storage_hub.modules.users.services.RegisterUserDataService;
import com.alisondev.live_storage_hub.security.JwtUtil;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user_data")
@Tag(name = "User Data", description = "Endpoints para dados de usuários.")
public class RegisterUserDataController {
  private final RegisterUserDataService registerUserDataService;
  private final JwtUtil jwtUtil;

  public RegisterUserDataController(RegisterUserDataService registerUserDataService, JwtUtil jwtUtil) {
    this.registerUserDataService = registerUserDataService;
    this.jwtUtil = jwtUtil;
  }

  @Operation(summary = "Registro de dados do usuário", description = "Realiza registro de dados do usuário.")
  @PostMapping("/register")
  public CustomApiResponse<UserDataResponseDTO> handle(@RequestHeader("Authorization") String authHeader,
      @RequestParam("userId") Long userId,
      @RequestBody RegisterUserDataDTO request) {
    String token = authHeader.substring(7);
    Long appId = jwtUtil.getAppIdFromToken(token);
    UserData data = registerUserDataService.execute(appId, userId, request);
    return CustomApiResponse.ok(toDto(data));
  }

  private UserDataResponseDTO toDto(UserData data) {
    UserDataResponseDTO dto = new UserDataResponseDTO();
    dto.setId(data.getId());
    dto.setJsonData(data.getJsonData());
    dto.setCreatedAt(data.getCreatedAt());
    return dto;
  }
}
