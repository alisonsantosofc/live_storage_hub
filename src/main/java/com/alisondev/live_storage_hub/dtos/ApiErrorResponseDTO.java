package com.alisondev.live_storage_hub.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "ApiErrorResponse", description = "Standard response returned whenever an API request fails.")
public class ApiErrorResponseDTO {
  @Schema(description = "Stable application error code.", example = "2.1.1")
  private String code;

  @Schema(description = "Human-readable explanation of the error.", example = "Invalid admin key.")
  private String message;

  @Schema(description = "Always null for an error response.", nullable = true)
  private Object data;
}
