package com.alisondev.live_storage_hub.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SendApiResponse<T> {
  private String code;
  private String message;
  private T data;

  public static <T> SendApiResponse<T> ok(T data) {
    return new SendApiResponse<>( "0", "Success", data);
  }

  public static <T> SendApiResponse<T> ok() {
    return new SendApiResponse<>( "0", "Success", null);
  }

  public static <T> SendApiResponse<T> ok(String message, T data) {
    return new SendApiResponse<>("0", message, data);
  }

  public static <T> SendApiResponse<T> error(String code, String message) {
    return new SendApiResponse<>(code, message, null);
  }
}