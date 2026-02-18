package com.alisondev.live_storage_hub.modules.users.controllers;

import com.alisondev.live_storage_hub.dtos.SendApiResponse;
import com.alisondev.live_storage_hub.modules.users.dtos.UserFileResponseDTO;
import com.alisondev.live_storage_hub.modules.users.entities.UserFile;
import com.alisondev.live_storage_hub.modules.users.services.RegisterUserFileService;
import com.alisondev.live_storage_hub.security.JwtUtil;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/users/{userId}/files")
@Tag(name = "User Files", description = "Endpoints for user files.")
public class RegisterUserFileController {
  private final RegisterUserFileService registerUserFileService;
  private final JwtUtil jwtUtil;

  public RegisterUserFileController(
    RegisterUserFileService registerUserFileService,
    JwtUtil jwtUtil
  ) {
    this.registerUserFileService = registerUserFileService;
    this.jwtUtil = jwtUtil;
  }

  @Operation(
    summary = "Upload user file",
    description = "Uploads and registers a new file for a user."
  )
  @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public SendApiResponse<UserFileResponseDTO> handle(
    @RequestHeader("Authorization") String authHeader,
    @PathVariable Long userId,
    @RequestPart("file") MultipartFile file,
    @RequestParam("fileType") String fileType
  ) throws IOException {
    String token = authHeader.replace("Bearer ", "");
    Long appId = jwtUtil.getAppIdFromToken(token);

    UserFile data = registerUserFileService.execute(
      appId,
      userId,
      file,
      fileType
    );

    return SendApiResponse.ok(toDto(data));
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
