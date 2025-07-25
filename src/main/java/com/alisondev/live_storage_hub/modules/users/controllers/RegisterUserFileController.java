package com.alisondev.live_storage_hub.modules.users.controllers;

import com.alisondev.live_storage_hub.modules.users.entities.UserFile;
import com.alisondev.live_storage_hub.modules.users.dtos.UserFileResponseDTO;
import com.alisondev.live_storage_hub.modules.users.services.RegisterUserFileService;
import com.alisondev.live_storage_hub.dtos.ApiResponse;
import com.alisondev.live_storage_hub.security.JwtUtil;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/user_file")
@Tag(name = "User File", description = "Endpoints for managing user files.")
public class RegisterUserFileController {
  private final RegisterUserFileService registerUserFileService;
  private final JwtUtil jwtUtil;

  @Autowired
  public RegisterUserFileController(RegisterUserFileService registerUserFileService, JwtUtil jwtUtil) {
    this.registerUserFileService = registerUserFileService;
    this.jwtUtil = jwtUtil;
  }

  @Operation(summary = "Upload user file", description = "Registers and upload new user file.")
  @PostMapping("/upload")
  public ApiResponse<UserFileResponseDTO> handle(@RequestHeader("Authorization") String authHeader,
      @RequestParam("userId") Long userId,
      @RequestParam("file") MultipartFile file,
      @RequestParam("fileType") String fileType) throws IOException {
    String token = authHeader.substring(7);
    Long appId = jwtUtil.getAppIdFromToken(token);

    UserFile data = registerUserFileService.execute(appId, userId, file, fileType);
    return ApiResponse.ok(toDto(data));
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
