package com.alisondev.live_storage_hub.modules.users.controllers;

import com.alisondev.live_storage_hub.dtos.SendApiResponse;
import com.alisondev.live_storage_hub.modules.users.dtos.RegisterUserDTO;
import com.alisondev.live_storage_hub.modules.users.dtos.RegisterUserResponseDTO;
import com.alisondev.live_storage_hub.dtos.ApiErrorResponseDTO;
import com.alisondev.live_storage_hub.modules.users.entities.User;
import com.alisondev.live_storage_hub.modules.users.services.RegisterUserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/users")
@Tag(name = "Users", description = "Endpoints for users.")
public class RegisterUserController {
  private final RegisterUserService registerUserService;

  public RegisterUserController(RegisterUserService registerUserService) {
    this.registerUserService = registerUserService;
  }

  @Operation(summary = "Register user", description = "Registers new user and return user info.")
  @ApiResponses({
      @ApiResponse(responseCode = "400", description = "Invalid API key (1.1.1) or email already registered (1.1.2).",
          content = @Content(schema = @Schema(implementation = ApiErrorResponseDTO.class),
              examples = {
                  @ExampleObject(name = "Invalid API key", value = "{\"code\":\"1.1.1\",\"message\":\"Invalid API key.\",\"data\":null}"),
                  @ExampleObject(name = "Email already registered", value = "{\"code\":\"1.1.2\",\"message\":\"User already registered for this app.\",\"data\":null}")
              }))
  })
  @PostMapping
  public SendApiResponse<RegisterUserResponseDTO> handle(@RequestHeader("X-Api-Key") String apiKey,
      @Valid @RequestBody RegisterUserDTO request) {
    User user = registerUserService.execute(apiKey, request);
    return SendApiResponse.ok("User registered successfully.", toDto(user));
  }

  private RegisterUserResponseDTO toDto(User user) {
    RegisterUserResponseDTO dto = new RegisterUserResponseDTO();
    dto.setId(user.getId());
    return dto;
  }
}
