package com.alisondev.live_storage_hub.modules.users.dtos;

import java.util.Map;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

@Data
public class RegisterUserDataDTO {
  @NotBlank
  @Size(max = 50)
  @Schema(description = "Data type for register.", example = "profile")
  private String dataType;
  @NotEmpty
  @Schema(description = "Json data for register.", example = "JSON.stringfy()")
  private Map<String, Object> jsonData;
}
