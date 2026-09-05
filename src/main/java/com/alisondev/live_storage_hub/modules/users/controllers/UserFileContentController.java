package com.alisondev.live_storage_hub.modules.users.controllers;

import com.alisondev.live_storage_hub.dtos.SendApiResponse;
import com.alisondev.live_storage_hub.modules.users.entities.User;
import com.alisondev.live_storage_hub.modules.users.services.UserFileStorageService;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@RestController
@RequestMapping("/users/files/{fileId}")
@Tag(name = "User Files", description = "Endpoints for user files.")
@SecurityRequirement(name = "bearerAuth")
public class UserFileContentController {
  private final UserFileStorageService userFileStorageService;

  public UserFileContentController(UserFileStorageService userFileStorageService) {
    this.userFileStorageService = userFileStorageService;
  }

  @GetMapping("/download")
  public ResponseEntity<byte[]> download(@AuthenticationPrincipal User authenticatedUser,
      @PathVariable Long fileId) throws IOException {
    Long userId = authenticatedUser.getId();
    var storedFile = userFileStorageService.download(authenticatedUser.getApp().getId(), userId, fileId);
    return ResponseEntity.ok()
        .contentType(MediaType.parseMediaType(storedFile.contentType()))
        .header(HttpHeaders.CONTENT_DISPOSITION, ContentDisposition.attachment()
            .filename(storedFile.originalName(), StandardCharsets.UTF_8).build().toString())
        .body(storedFile.content());
  }

  @DeleteMapping
  public SendApiResponse<Void> delete(@AuthenticationPrincipal User authenticatedUser,
      @PathVariable Long fileId) throws IOException {
    Long userId = authenticatedUser.getId();
    userFileStorageService.delete(authenticatedUser.getApp().getId(), userId, fileId);
    return SendApiResponse.ok("File deleted successfully.", null);
  }
}
