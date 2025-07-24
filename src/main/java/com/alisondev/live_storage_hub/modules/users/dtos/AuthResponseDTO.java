package com.alisondev.live_storage_hub.modules.users.dtos;

import lombok.Data;

@Data
public class AuthResponseDTO {
  private String token;
  private UserResponseDTO user;
}
