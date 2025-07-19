package com.alisondev.live_storage_hub.dto;

import lombok.Data;

@Data
public class LoginRequest {
  private String email;
  private String password;
}
