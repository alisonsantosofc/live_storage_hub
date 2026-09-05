package com.alisondev.live_storage_hub.modules.users.controllers;

import com.alisondev.live_storage_hub.dtos.SendApiResponse;
import com.alisondev.live_storage_hub.modules.users.dtos.UserDataResponseDTO;
import com.alisondev.live_storage_hub.modules.users.entities.UserData;
import com.alisondev.live_storage_hub.modules.users.services.ListUserDataService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import com.alisondev.live_storage_hub.modules.users.entities.User;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/users/data")
@Tag(name = "User Data", description = "Endpoints for user data.")
@SecurityRequirement(name = "bearerAuth")
public class ListUserDataController {

  private final ListUserDataService listUserDataService;

  public ListUserDataController(
    ListUserDataService listUserDataService
  ) {
    this.listUserDataService = listUserDataService;
  }

  @Operation(
    summary = "List user data",
    description = "Lists all data entries associated with a user."
  )
  @GetMapping
  public SendApiResponse<List<UserDataResponseDTO>> handle(
    @AuthenticationPrincipal User authenticatedUser
  ) {
    Long appId = authenticatedUser.getApp().getId();
    Long userId = authenticatedUser.getId();

    List<UserDataResponseDTO> list = listUserDataService
      .execute(appId, userId)
      .stream()
      .map(this::toDto)
      .collect(Collectors.toList());

    return SendApiResponse.ok("User data listed successfully.", list);
  }

  private UserDataResponseDTO toDto(UserData data) {
    UserDataResponseDTO dto = new UserDataResponseDTO();
    dto.setId(data.getId());
    dto.setJsonData(data.getJsonData());
    dto.setCreatedAt(data.getCreatedAt());
    return dto;
  }
}
