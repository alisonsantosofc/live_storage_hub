package com.alisondev.live_storage_hub.modules.users.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Data
public class RegisterUserDTO {
  @NotBlank
  @Size(max = 255)
  @Schema(description = "User name.", example = "John Doe")
  private String name;

  @NotBlank
  @Email
  @Size(max = 255)
  @Schema(description = "User email.", example = "johndoe@email.com")
  private String email;

  @NotBlank
  @Size(min = 8, max = 72)
  @Schema(description = "User password.", example = "********")
  private String password;
}
