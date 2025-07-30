package com.alisondev.live_storage_hub.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import com.alisondev.live_storage_hub.dtos.SendApiResponse;

@ControllerAdvice
public class GlobalExceptionHandler {
  @ExceptionHandler(RuntimeException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public SendApiResponse<String> handleRuntimeException(RuntimeException ex) {
    return SendApiResponse.error(ex.getMessage());
  }
}
