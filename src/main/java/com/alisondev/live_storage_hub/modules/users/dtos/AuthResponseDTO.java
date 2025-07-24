package com.alisondev.live_storage_hub.modules.users.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class AuthResponseDTO {
  @Schema(description = "API token associated with the user.", example = "abc123xyz456...")
  private String token;

  @Schema(description = "Authenticated user data.")
  private UserResponseDTO user;
}
