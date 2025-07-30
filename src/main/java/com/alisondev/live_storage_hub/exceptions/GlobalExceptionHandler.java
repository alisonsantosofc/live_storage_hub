package com.alisondev.live_storage_hub.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import com.alisondev.live_storage_hub.dtos.SendApiResponse;

@ControllerAdvice
public class GlobalExceptionHandler {
  @ExceptionHandler(ApiRuntimeException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public SendApiResponse<String> handleApiRuntimeException(ApiRuntimeException ex) {
    return SendApiResponse.error(ex.getCode(), ex.getMessage());
  }

  @ExceptionHandler(RuntimeException.class)
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  public SendApiResponse<String> handleRuntimeException(RuntimeException ex) {
    return SendApiResponse.error("0.0.0", "Unexpected error: " + ex.getMessage());
  }
}
