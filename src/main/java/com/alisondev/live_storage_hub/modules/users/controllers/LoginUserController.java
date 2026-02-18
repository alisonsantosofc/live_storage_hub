package com.alisondev.live_storage_hub.modules.users.controllers;

import com.alisondev.live_storage_hub.dtos.SendApiResponse;
import com.alisondev.live_storage_hub.modules.users.dtos.LoginUserResponseDTO;
import com.alisondev.live_storage_hub.modules.users.dtos.LoginUserDTO;
import com.alisondev.live_storage_hub.modules.users.services.LoginUserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.web.bind.annotation.*;

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
    @ApiResponse(
      responseCode = "401",
      description = "Invalid credentials"
    ),
    @ApiResponse(
      responseCode = "403",
      description = "Invalid API key"
    )
  })
  @PostMapping
  public SendApiResponse<LoginUserResponseDTO> handle(
    @RequestHeader("X-Api-Key") String apiKey,
    @RequestBody LoginUserDTO request
  ) {
    return SendApiResponse.ok(
      loginUserService.execute(apiKey, request)
    );
  }
}
