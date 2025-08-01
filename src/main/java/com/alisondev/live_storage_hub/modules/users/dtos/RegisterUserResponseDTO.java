package com.alisondev.live_storage_hub.modules.users.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class RegisterUserResponseDTO {
  @Schema(description = "Unique user identifier code.", example = "123...")
  private Long id;
}
