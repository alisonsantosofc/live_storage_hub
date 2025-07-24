package com.alisondev.live_storage_hub.modules.users.dtos;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.Map;

@Data
public class UserFileResponseDTO {
  private Long id;
  private String fileType;
  private String fileUrl;
  private Map<String, Object> metadata;
  private LocalDateTime createdAt;
}
