package com.alisondev.live_storage_hub.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CustomApiResponse<T> {
  private boolean success;
  private String message;
  private T data;

  public static <T> CustomApiResponse<T> ok(T data) {
    return new CustomApiResponse<>(true, "Success", data);
  }

  public static <T> CustomApiResponse<T> error(String message) {
    return new CustomApiResponse<>(false, message, null);
  }
}
