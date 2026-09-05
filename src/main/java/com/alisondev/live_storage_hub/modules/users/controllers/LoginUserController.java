package com.alisondev.live_storage_hub.modules.users.controllers;

import com.alisondev.live_storage_hub.dtos.SendApiResponse;
import com.alisondev.live_storage_hub.modules.users.dtos.LoginUserResponseDTO;
import com.alisondev.live_storage_hub.modules.users.dtos.LoginUserDTO;
import com.alisondev.live_storage_hub.modules.users.services.LoginUserService;
import com.alisondev.live_storage_hub.dtos.ApiErrorResponseDTO;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/auth")
@Tag(name = "Authentication", description = "Endpoints for authenticate users.")
public class LoginUserController {
  private final LoginUserService loginUserService;

  public LoginUserController(LoginUserService loginUserService) {
    this.loginUserService = loginUserService;
  }

  @Operation(summary = "Login user", description = "Authenticate user and return JWT token.")
  @ApiResponses(value = {
    @ApiResponse(
      responseCode = "200",
      description = "Successful login",
      content = @Content(schema = @Schema(implementation = LoginUserResponseDTO.class))
    ),
    @ApiResponse(responseCode = "400", description = "Invalid API key or credentials (1.2.1, 1.2.2, or 1.2.3).",
      content = @Content(schema = @Schema(implementation = ApiErrorResponseDTO.class),
        examples = @ExampleObject(value = "{\"code\":\"1.2.3\",\"message\":\"Invalid email or password.\",\"data\":null}")))
  })
  @PostMapping
  public SendApiResponse<LoginUserResponseDTO> handle(
    @RequestHeader("X-Api-Key") String apiKey,
    @Valid @RequestBody LoginUserDTO request
  ) {
    return SendApiResponse.ok("User authenticated successfully.",
      loginUserService.execute(apiKey, request)
    );
  }
}
