package com.alisondev.live_storage_hub.modules.apps.dtos;

import lombok.Data;
import java.time.LocalDateTime;

import io.swagger.v3.oas.annotations.media.Schema;

@Data
public class AppResponseDTO {
  @Schema(description = "Unique app identifier code.", example = "123...")
  private Long id;

  @Schema(description = "App name.", example = "MyApp")
  private String name;

  @Schema(description = "API key associated with the app.", example = "abc123xyz456...")
  private String apiKey;

  @Schema(description = "App creation date and time.", example = "0000-00-00T00:00:00")
  private LocalDateTime createdAt;
}
