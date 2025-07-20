package com.alisondev.live_storage_hub.modules.users.dtos.user;

import lombok.Data;

@Data
public class UserRegisterRequest {
  private String name;
  private String email;
  private String password;
}