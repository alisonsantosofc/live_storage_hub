package com.alisondev.live_storage_hub.modules.users.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@Data
public class LoginUserDTO {
  @NotBlank
  @Email
  @Schema(description = "Registered user email.", example = "johndoe@email.com")
  private String email;

  @NotBlank
  @Schema(description = "Registered user password.", example = "********")
  private String password;
}
