package com.alisondev.live_storage_hub.exception;

import com.alisondev.live_storage_hub.dto.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@ControllerAdvice
public class GlobalExceptionHandler {
  @ExceptionHandler(RuntimeException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ApiResponse<String> handleRuntimeException(RuntimeException ex) {
    return ApiResponse.error(ex.getMessage());
  }
}
