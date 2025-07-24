package com.alisondev.live_storage_hub.modules.users.dtos;

import java.time.LocalDateTime;
import java.util.Map;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class UserDataResponseDTO {
  @Schema(description = "Unique user data identifier code.", example = "123...")
  private Long id;

  @Schema(description = "Additional data in JSON format, to store custom user data", example = "{\"lang\":\"en-us\", \"limit_per_day\": 10}")
  private Map<String, Object> jsonData;

  @Schema(description = "User data creation date and time.", example = "0000-00-00T00:00:00")
  private LocalDateTime createdAt;
}