package com.alisondev.live_storage_hub.modules.users.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class LoginUserDTO {
  @Schema(description = "Registered user email.", example = "johndoe@email.com")
  private String email;

  @Schema(description = "Registered user password.", example = "********")
  private String password;
}
