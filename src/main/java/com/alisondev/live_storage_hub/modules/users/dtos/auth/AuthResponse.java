package com.alisondev.live_storage_hub.modules.users.dtos.auth;

import com.alisondev.live_storage_hub.dtos.user.UserResponse;

import lombok.Data;

@Data
public class AuthResponse {
  private String token;
  private UserResponse user;
}
