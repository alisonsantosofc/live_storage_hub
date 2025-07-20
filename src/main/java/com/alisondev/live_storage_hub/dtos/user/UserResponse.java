package com.alisondev.live_storage_hub.dtos.user;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class UserResponse {
  private Long id;
  private String name;
  private String email;
  private LocalDateTime createdAt;
}
