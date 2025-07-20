package com.alisondev.live_storage_hub.dto.user_data;

import java.time.LocalDateTime;
import java.util.Map;

import lombok.Data;

@Data
public class UserDataResponse {
  private Long id;
  private Map<String, Object> jsonData;
  private LocalDateTime createdAt;
}