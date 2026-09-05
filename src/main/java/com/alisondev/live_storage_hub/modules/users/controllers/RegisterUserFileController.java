package com.alisondev.live_storage_hub.modules.users.controllers;

import com.alisondev.live_storage_hub.dtos.SendApiResponse;
import com.alisondev.live_storage_hub.modules.users.dtos.UserFileResponseDTO;
import com.alisondev.live_storage_hub.modules.users.entities.UserFile;
import com.alisondev.live_storage_hub.modules.users.services.RegisterUserFileService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import com.alisondev.live_storage_hub.modules.users.entities.User;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/users/files")
@Tag(name = "User Files", description = "Endpoints for user files.")
@SecurityRequirement(name = "bearerAuth")
public class RegisterUserFileController {
  private final RegisterUserFileService registerUserFileService;

  public RegisterUserFileController(
    RegisterUserFileService registerUserFileService
  ) {
    this.registerUserFileService = registerUserFileService;
  }

  @Operation(
    summary = "Upload user file",
    description = "Uploads and registers a new file for a user."
  )
  @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public SendApiResponse<UserFileResponseDTO> handle(
    @AuthenticationPrincipal User authenticatedUser,
    @RequestPart("file") MultipartFile file,
    @RequestParam("fileType") String fileType
  ) throws IOException {
    Long appId = authenticatedUser.getApp().getId();
    Long userId = authenticatedUser.getId();

    UserFile data = registerUserFileService.execute(
      appId,
      userId,
      file,
      fileType
    );

    return SendApiResponse.ok("File uploaded successfully.", toDto(data));
  }

  private UserFileResponseDTO toDto(UserFile entity) {
    UserFileResponseDTO dto = new UserFileResponseDTO();
    dto.setId(entity.getId());
    dto.setFileType(entity.getFileType());
    dto.setFileUrl("/users/files/" + entity.getId() + "/download");
    dto.setMetadata(entity.getMetadata());
    dto.setCreatedAt(entity.getCreatedAt());
    return dto;
  }
}
