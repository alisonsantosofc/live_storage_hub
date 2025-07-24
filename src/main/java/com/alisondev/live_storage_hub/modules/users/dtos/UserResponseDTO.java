package com.alisondev.live_storage_hub.modules.users.dtos;

import java.time.LocalDateTime;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class UserResponseDTO {
  @Schema(description = "Unique user identifier code.", example = "123...")
  private Long id;

  @Schema(description = "User name.", example = "John Doe")
  private String name;

  @Schema(description = "User email.", example = "johndoe@email.com")
  private String email;

  @Schema(description = "User creation date and time.", example = "0000-00-00T00:00:00")
  private LocalDateTime createdAt;
}
