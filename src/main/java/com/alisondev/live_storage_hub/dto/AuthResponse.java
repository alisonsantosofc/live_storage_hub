package com.alisondev.live_storage_hub.dto;

import lombok.Data;

@Data
public class AuthResponse {
  private String token;
  private UserResponse user;
}
