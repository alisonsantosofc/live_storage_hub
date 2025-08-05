package com.alisondev.live_storage_hub.modules.users.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class SendVerificationCodeDTO {
  @Schema(description = "User email.", example = "johndoe@email.com")
  private String email;
}