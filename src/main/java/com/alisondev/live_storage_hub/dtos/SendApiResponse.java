package com.alisondev.live_storage_hub.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import io.swagger.v3.oas.annotations.media.Schema;

@Data
@AllArgsConstructor
public class SendApiResponse<T> {
  @Schema(description = "Application result code. `0` indicates success; dotted values identify an error.", example = "0")
  private String code;
  @Schema(description = "Human-readable result message.", example = "Success")
  private String message;
  @Schema(description = "Response data. It is null when a request fails.", nullable = true)
  private T data;

  public static <T> SendApiResponse<T> ok() {
    return new SendApiResponse<>( "0", "Success", null);
  }

  public static <T> SendApiResponse<T> ok(T data) {
    return new SendApiResponse<>( "0", "Success", data);
  }

  public static <T> SendApiResponse<T> ok(String message, T data) {
    return new SendApiResponse<>("0", message, data);
  }

  public static <T> SendApiResponse<T> error(String code, String message) {
    return new SendApiResponse<>(code, message, null);
  }
}
