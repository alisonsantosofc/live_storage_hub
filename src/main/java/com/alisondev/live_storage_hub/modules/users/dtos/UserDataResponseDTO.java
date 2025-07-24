package com.alisondev.live_storage_hub.modules.users.dtos;

import java.time.LocalDateTime;
import java.util.Map;

import lombok.Data;

@Data
public class UserDataResponseDTO {
  private Long id;
  private Map<String, Object> jsonData;
  private LocalDateTime createdAt;
}