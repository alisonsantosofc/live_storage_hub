package com.alisondev.live_storage_hub.modules.users.controllers;

import com.alisondev.live_storage_hub.dtos.SendApiResponse;
import com.alisondev.live_storage_hub.modules.users.dtos.UserFileResponseDTO;
import com.alisondev.live_storage_hub.modules.users.entities.UserFile;
import com.alisondev.live_storage_hub.modules.users.services.ListUserFileService;
import com.alisondev.live_storage_hub.security.JwtUtil;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/users/{userId}/files")
@Tag(name = "User Files", description = "Endpoints for user files.")
public class ListUserFileController {
  private final ListUserFileService listUserFileService;
  private final JwtUtil jwtUtil;

  public ListUserFileController(                            
    ListUserFileService listUserFileService,
    JwtUtil jwtUtil
  ) {
    this.listUserFileService = listUserFileService;
    this.jwtUtil = jwtUtil;
  }

  @Operation(
    summary = "List user files",
    description = "Lists all files registered for a user."
  )
  @GetMapping
  public SendApiResponse<List<UserFileResponseDTO>> handle(
    @RequestHeader("Authorization") String authHeader,
    @PathVariable Long userId
  ) {
    String token = authHeader.replace("Bearer ", "");
    Long appId = jwtUtil.getAppIdFromToken(token);

    List<UserFileResponseDTO> list = listUserFileService
      .execute(appId, userId)
      .stream()
      .map(this::toDto)
      .collect(Collectors.toList());

    return SendApiResponse.ok(list);
  }

  private UserFileResponseDTO toDto(UserFile entity) {
    UserFileResponseDTO dto = new UserFileResponseDTO();
    dto.setId(entity.getId());
    dto.setFileType(entity.getFileType());
    dto.setFileUrl(entity.getFileUrl());
    dto.setMetadata(entity.getMetadata());
    dto.setCreatedAt(entity.getCreatedAt());
    return dto;
  }
}
