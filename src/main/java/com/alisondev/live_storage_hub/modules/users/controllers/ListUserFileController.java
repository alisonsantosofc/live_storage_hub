package com.alisondev.live_storage_hub.modules.users.controllers;

import com.alisondev.live_storage_hub.dtos.SendApiResponse;
import com.alisondev.live_storage_hub.modules.users.dtos.UserFileResponseDTO;
import com.alisondev.live_storage_hub.modules.users.entities.UserFile;
import com.alisondev.live_storage_hub.modules.users.services.ListUserFileService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import com.alisondev.live_storage_hub.modules.users.entities.User;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/users/files")
@Tag(name = "User Files", description = "Endpoints for user files.")
@SecurityRequirement(name = "bearerAuth")
public class ListUserFileController {
  private final ListUserFileService listUserFileService;

  public ListUserFileController(                            
    ListUserFileService listUserFileService
  ) {
    this.listUserFileService = listUserFileService;
  }

  @Operation(
    summary = "List user files",
    description = "Lists all files registered for a user."
  )
  @GetMapping
  public SendApiResponse<List<UserFileResponseDTO>> handle(
    @AuthenticationPrincipal User authenticatedUser
  ) {
    Long appId = authenticatedUser.getApp().getId();
    Long userId = authenticatedUser.getId();

    List<UserFileResponseDTO> list = listUserFileService
      .execute(appId, userId)
      .stream()
      .map(this::toDto)
      .collect(Collectors.toList());

    return SendApiResponse.ok("Files listed successfully.", list);
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
