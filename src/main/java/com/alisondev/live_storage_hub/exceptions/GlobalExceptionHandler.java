package com.alisondev.live_storage_hub.exceptions;

import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.io.IOException;

import com.alisondev.live_storage_hub.dtos.SendApiResponse;

@Hidden
@RestControllerAdvice
public class GlobalExceptionHandler {
  @ExceptionHandler(ApiRuntimeException.class)
  public ResponseEntity<SendApiResponse<Void>> handleApiRuntimeException(ApiRuntimeException ex) {
    return ResponseEntity.status(ex.getStatus())
        .body(SendApiResponse.error(ex.getCode(), ex.getMessage()));
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public SendApiResponse<Void> handleValidation(MethodArgumentNotValidException ex) {
    FieldError error = ex.getBindingResult().getFieldError();
    String field = error == null ? "request" : error.getField();
    String message = error == null ? "Invalid request data." : error.getDefaultMessage();
    return SendApiResponse.error("0.0.1", "Invalid " + field + ": " + message);
  }

  @ExceptionHandler(MissingRequestHeaderException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public SendApiResponse<Void> handleMissingHeader(MissingRequestHeaderException ex) {
    return SendApiResponse.error("0.0.2", "Required header '" + ex.getHeaderName() + "' was not provided.");
  }

  @ExceptionHandler(MissingServletRequestParameterException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public SendApiResponse<Void> handleMissingParameter(MissingServletRequestParameterException ex) {
    return SendApiResponse.error("0.0.3", "Required parameter '" + ex.getParameterName() + "' was not provided.");
  }

  @ExceptionHandler(HttpMessageNotReadableException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public SendApiResponse<Void> handleUnreadableBody(HttpMessageNotReadableException ex) {
    return SendApiResponse.error("0.0.4", "Request body is missing or contains invalid JSON.");
  }

  @ExceptionHandler(MaxUploadSizeExceededException.class)
  @ResponseStatus(HttpStatus.PAYLOAD_TOO_LARGE)
  public SendApiResponse<Void> handleUploadTooLarge(MaxUploadSizeExceededException ex) {
    return SendApiResponse.error("0.0.5", "Uploaded file exceeds the configured size limit.");
  }

  @ExceptionHandler(MissingServletRequestPartException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public SendApiResponse<Void> handleMissingUploadPart(MissingServletRequestPartException ex) {
    return SendApiResponse.error("0.0.6", "Required multipart part '" + ex.getRequestPartName() + "' was not provided.");
  }

  @ExceptionHandler(MultipartException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public SendApiResponse<Void> handleMultipart(MultipartException ex) {
    return SendApiResponse.error("0.0.7", "Invalid multipart upload request.");
  }

  @ExceptionHandler({MethodArgumentTypeMismatchException.class, MissingPathVariableException.class})
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public SendApiResponse<Void> handleInvalidPathValue(Exception ex) {
    return SendApiResponse.error("0.0.8", "One or more path or query parameter values are invalid.");
  }

  @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
  @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
  public SendApiResponse<Void> handleMethodNotAllowed(HttpRequestMethodNotSupportedException ex) {
    return SendApiResponse.error("0.0.9", "HTTP method is not supported for this resource.");
  }

  @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
  @ResponseStatus(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
  public SendApiResponse<Void> handleMediaType(HttpMediaTypeNotSupportedException ex) {
    return SendApiResponse.error("0.0.10", "Content type is not supported for this resource.");
  }

  @ExceptionHandler(NoResourceFoundException.class)
  @ResponseStatus(HttpStatus.NOT_FOUND)
  public SendApiResponse<Void> handleNotFound(NoResourceFoundException ex) {
    return SendApiResponse.error("0.0.11", "Resource was not found.");
  }

  @ExceptionHandler(IOException.class)
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  public SendApiResponse<Void> handleStorageIOException(IOException ex) {
    return SendApiResponse.error("0.0.12", "Unable to process the stored file.");
  }

  @ExceptionHandler(DataIntegrityViolationException.class)
  @ResponseStatus(HttpStatus.CONFLICT)
  public SendApiResponse<Void> handleDataIntegrity(DataIntegrityViolationException ex) {
    return SendApiResponse.error("0.0.13", "The request conflicts with existing data.");
  }

  @ExceptionHandler(RuntimeException.class)
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  public SendApiResponse<Void> handleRuntimeException(RuntimeException ex) {
    return SendApiResponse.error("0.0.0", "Unexpected internal error.");
  }
}
