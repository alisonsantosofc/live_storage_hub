package com.alisondev.live_storage_hub.modules.apps.dtos;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class AppResponse {
  private Long id;
  private String name;
  private String apiKey;
  private LocalDateTime createdAt;
}
