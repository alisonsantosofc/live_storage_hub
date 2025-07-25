package com.alisondev.live_storage_hub.modules.users.controllers;

import com.alisondev.live_storage_hub.modules.users.entities.UserFile;
import com.alisondev.live_storage_hub.modules.users.dtos.UserFileResponseDTO;
import com.alisondev.live_storage_hub.modules.users.services.ListUserFileService;
import com.alisondev.live_storage_hub.dtos.ApiResponse;
import com.alisondev.live_storage_hub.security.JwtUtil;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/user_file")
@Tag(name = "User File", description = "Endpoints for managing user files.")
public class ListUserFileController {
  private final ListUserFileService listUserFileService;
  private final JwtUtil jwtUtil;

  @Autowired
  public ListUserFileController(ListUserFileService listUserFileService, JwtUtil jwtUtil) {
    this.listUserFileService = listUserFileService;
    this.jwtUtil = jwtUtil;
  }

  @Operation(summary = "List user file", description = "Lists all registered user file.")
  @GetMapping("/list")
  public ApiResponse<List<UserFileResponseDTO>> handle(@RequestHeader("Authorization") String authHeader,
      @RequestParam Long userId) {
    String token = authHeader.substring(7);
    Long appId = jwtUtil.getAppIdFromToken(token);

    List<UserFileResponseDTO> list = listUserFileService.execute(appId, userId)
        .stream().map(this::toDto).collect(Collectors.toList());

    return ApiResponse.ok(list);
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
