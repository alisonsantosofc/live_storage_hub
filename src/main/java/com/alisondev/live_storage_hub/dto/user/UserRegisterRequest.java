package com.alisondev.live_storage_hub.dto.user;

import lombok.Data;

@Data
public class UserRegisterRequest {
  private String name;
  private String email;
  private String password;
}