package com.alisondev.live_storage_hub.modules.users.controllers;

import com.alisondev.live_storage_hub.dtos.SendApiResponse;
import com.alisondev.live_storage_hub.modules.users.dtos.RegisterUserDataDTO;
import com.alisondev.live_storage_hub.modules.users.dtos.RegisterUserDataResponseDTO;
import com.alisondev.live_storage_hub.modules.users.entities.UserData;
import com.alisondev.live_storage_hub.modules.users.services.RegisterUserDataService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import com.alisondev.live_storage_hub.modules.users.entities.User;

@RestController
@RequestMapping("/users/data")
@Tag(name = "User Data", description = "Endpoints for user data.")
@SecurityRequirement(name = "bearerAuth")
public class RegisterUserDataController {
  private final RegisterUserDataService registerUserDataService;

  public RegisterUserDataController(
    RegisterUserDataService registerUserDataService
  ) {
    this.registerUserDataService = registerUserDataService;
  }

  @Operation(
    summary = "Create user data",
    description = "Creates a new data entry associated with a user."
  )
  @PostMapping
  public SendApiResponse<RegisterUserDataResponseDTO> handle(
    @AuthenticationPrincipal User authenticatedUser,
    @Valid @RequestBody RegisterUserDataDTO request
  ) {
    Long appId = authenticatedUser.getApp().getId();
    Long userId = authenticatedUser.getId();

    UserData data = registerUserDataService.execute(appId, userId, request);

    return SendApiResponse.ok("User data registered successfully.", toDto(data));
  }

  private RegisterUserDataResponseDTO toDto(UserData data) {
    RegisterUserDataResponseDTO dto = new RegisterUserDataResponseDTO();
    dto.setId(data.getId());
    return dto;
  }
}
