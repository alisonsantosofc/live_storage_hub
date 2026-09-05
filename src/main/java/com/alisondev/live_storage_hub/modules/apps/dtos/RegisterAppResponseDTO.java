package com.alisondev.live_storage_hub.modules.apps.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class RegisterAppResponseDTO {
  @Schema(description = "Unique app identifier code.", example = "123...")
  private Long id;
  @Schema(description = "Unique app api key.", example = "hs113hbj...")
  private String apiKey;
}
