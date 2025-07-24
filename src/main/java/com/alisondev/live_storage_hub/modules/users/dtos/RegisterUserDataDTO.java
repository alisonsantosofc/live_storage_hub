package com.alisondev.live_storage_hub.modules.users.dtos;

import java.util.Map;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class RegisterUserDataDTO {
  @Schema(description = "Json data for register.", example = "JSON.stringfy()")
  private Map<String, Object> jsonData;
}
