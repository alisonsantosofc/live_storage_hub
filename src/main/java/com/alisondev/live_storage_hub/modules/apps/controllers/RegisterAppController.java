package com.alisondev.live_storage_hub.modules.apps.controllers;

import com.alisondev.live_storage_hub.dtos.SendApiResponse;
import com.alisondev.live_storage_hub.modules.apps.dtos.RegisterAppDTO;
import com.alisondev.live_storage_hub.modules.apps.dtos.RegisterAppResponseDTO;
import com.alisondev.live_storage_hub.dtos.ApiErrorResponseDTO;
import com.alisondev.live_storage_hub.modules.apps.entities.App;
import com.alisondev.live_storage_hub.modules.apps.services.RegisterAppService;

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
@RequestMapping("/apps")
@Tag(name = "Apps", description = "Endpoints for apps.")
public class RegisterAppController {
  private final RegisterAppService registerAppService;

  public RegisterAppController(RegisterAppService registerAppService) {
    this.registerAppService = registerAppService;
  }

  @PostMapping
  @Operation(summary = "Register app", description = "Registers new apps.")
  @ApiResponses({
      @ApiResponse(responseCode = "400", description = "Invalid admin key (2.1.1).",
          content = @Content(schema = @Schema(implementation = ApiErrorResponseDTO.class),
              examples = @ExampleObject(value = "{\"code\":\"2.1.1\",\"message\":\"Invalid admin key.\",\"data\":null}")))
  })
  public SendApiResponse<RegisterAppResponseDTO> handle(
    @RequestHeader("X-Admin-Key") String adminKey,
    @Valid @RequestBody RegisterAppDTO body) {

    App app = registerAppService.execute(adminKey, body.getName());
    return SendApiResponse.ok("App registered successfully.", toDto(app));
  }

  private RegisterAppResponseDTO toDto(App app) {
    RegisterAppResponseDTO dto = new RegisterAppResponseDTO();
    dto.setId(app.getId());
    dto.setApiKey(app.getApiKey());
    return dto;
  }
}
