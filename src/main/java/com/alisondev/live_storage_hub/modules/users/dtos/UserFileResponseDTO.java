package com.alisondev.live_storage_hub.modules.users.dtos;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.Map;

import io.swagger.v3.oas.annotations.media.Schema;

@Data
public class UserFileResponseDTO {
  @Schema(description = "Unique user file identifier code.", example = "123...")
  private Long id;

  @Schema(description = "User file type.", example = "'backup', 'avatar', 'config'")
  private String fileType;

  @Schema(description = "Public url or path to access the file", example = "https://livestoragehub.com/files/file_name.jpeg")
  private String fileUrl;

  @Schema(description = "Additional data in JSON format, to store custom user file data", example = "{\"resolution\":\"1920x1080\", \"size\": 2048}")
  private Map<String, Object> metadata;

  @Schema(description = "User file upload date and time.", example = "0000-00-00T00:00:00")
  private LocalDateTime createdAt;
}
