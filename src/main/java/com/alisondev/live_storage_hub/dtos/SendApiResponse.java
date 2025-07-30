package com.alisondev.live_storage_hub.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SendApiResponse<T> {
  private boolean success;
  private String message;
  private T data;

  public static <T> SendApiResponse<T> ok(T data) {
    return new SendApiResponse<>(true, "Success", data);
  }

  public static <T> SendApiResponse<T> error(String message) {
    return new SendApiResponse<>(false, message, null);
  }
}
