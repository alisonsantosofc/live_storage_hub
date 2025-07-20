package com.alisondev.live_storage_hub.dto;

import com.alisondev.live_storage_hub.dto.user.UserResponse;

import lombok.Data;

@Data
public class AuthResponse {
  private String token;
  private UserResponse user;
}
