package com.alisondev.live_storage_hub.modules.users.controllers;

import com.alisondev.live_storage_hub.dtos.SendApiResponse;
import com.alisondev.live_storage_hub.modules.users.dtos.RegisterUserDataDTO;
import com.alisondev.live_storage_hub.modules.users.dtos.RegisterUserDataResponseDTO;
import com.alisondev.live_storage_hub.modules.users.entities.UserData;
import com.alisondev.live_storage_hub.modules.users.services.RegisterUserDataService;
import com.alisondev.live_storage_hub.security.JwtUtil;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users/{userId}/data")
@Tag(name = "User Data", description = "Endpoints for user data.")
public class RegisterUserDataController {
  private final RegisterUserDataService registerUserDataService;
  private final JwtUtil jwtUtil;

  public RegisterUserDataController(
    RegisterUserDataService registerUserDataService,
    JwtUtil jwtUtil
  ) {
    this.registerUserDataService = registerUserDataService;
    this.jwtUtil = jwtUtil;
  }

  @Operation(
    summary = "Create user data",
    description = "Creates a new data entry associated with a user."
  )
  @PostMapping
  public SendApiResponse<RegisterUserDataResponseDTO> handle(
    @RequestHeader("Authorization") String authHeader,
    @PathVariable Long userId,
    @RequestBody RegisterUserDataDTO request
  ) {
    String token = authHeader.replace("Bearer ", "");
    Long appId = jwtUtil.getAppIdFromToken(token);

    UserData data = registerUserDataService.execute(appId, userId, request);

    return SendApiResponse.ok(toDto(data));
  }

  private RegisterUserDataResponseDTO toDto(UserData data) {
    RegisterUserDataResponseDTO dto = new RegisterUserDataResponseDTO();
    dto.setId(data.getId());
    return dto;
  }
}
