package com.alisondev.live_storage_hub.modules.users.dtos;

import lombok.Data;

@Data
public class RegisterUserDTO {
  private String name;
  private String email;
  private String password;
}