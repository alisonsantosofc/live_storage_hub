package com.alisondev.live_storage_hub.modules.apps.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class RegisterAppDTO {
  @Schema(description = "App name for register.", example = "MyApp")
  private String name;
}