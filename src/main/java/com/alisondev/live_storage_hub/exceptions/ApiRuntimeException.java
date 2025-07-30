package com.alisondev.live_storage_hub.exceptions;

public class ApiRuntimeException extends RuntimeException {
  private final String code;

  public ApiRuntimeException(String code, String message) {
    super(message);
    this.code = code;
  }

  public String getCode() {
    return code;
  }
}