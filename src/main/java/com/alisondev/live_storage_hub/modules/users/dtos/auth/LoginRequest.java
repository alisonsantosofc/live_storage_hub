package com.alisondev.live_storage_hub.modules.users.dtos.auth;

import lombok.Data;

@Data
public class LoginRequest {
  private String email;
  private String password;
}
