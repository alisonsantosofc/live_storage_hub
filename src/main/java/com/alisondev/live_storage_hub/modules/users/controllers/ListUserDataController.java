package com.alisondev.live_storage_hub.modules.users.controllers;

import com.alisondev.live_storage_hub.dtos.SendApiResponse;
import com.alisondev.live_storage_hub.modules.users.dtos.UserDataResponseDTO;
import com.alisondev.live_storage_hub.modules.users.entities.UserData;
import com.alisondev.live_storage_hub.modules.users.services.ListUserDataService;
import com.alisondev.live_storage_hub.security.JwtUtil;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/users/{userId}/data")
@Tag(name = "User Data", description = "Endpoints for user data.")
public class ListUserDataController {

  private final ListUserDataService listUserDataService;
  private final JwtUtil jwtUtil;

  public ListUserDataController(
    ListUserDataService listUserDataService,
    JwtUtil jwtUtil
  ) {
    this.listUserDataService = listUserDataService;
    this.jwtUtil = jwtUtil;
  }

  @Operation(
    summary = "List user data",
    description = "Lists all data entries associated with a user."
  )
  @GetMapping
  public SendApiResponse<List<UserDataResponseDTO>> handle(
    @RequestHeader("Authorization") String authHeader,
    @PathVariable Long userId
  ) {
    String token = authHeader.replace("Bearer ", "");
    Long appId = jwtUtil.getAppIdFromToken(token);

    List<UserDataResponseDTO> list = listUserDataService
      .execute(appId, userId)
      .stream()
      .map(this::toDto)
      .collect(Collectors.toList());

    return SendApiResponse.ok(list);
  }

  private UserDataResponseDTO toDto(UserData data) {
    UserDataResponseDTO dto = new UserDataResponseDTO();
    dto.setId(data.getId());
    dto.setJsonData(data.getJsonData());
    dto.setCreatedAt(data.getCreatedAt());
    return dto;
  }
}
