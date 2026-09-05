package com.alisondev.live_storage_hub.modules.apps.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Data
public class RegisterAppDTO {
  @NotBlank
  @Size(max = 255)
  @Schema(description = "App name for register.", example = "MyApp")
  private String name;
}
