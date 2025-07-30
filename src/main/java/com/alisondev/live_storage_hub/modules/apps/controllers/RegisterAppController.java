package com.alisondev.live_storage_hub.modules.apps.controllers;

import com.alisondev.live_storage_hub.dtos.SendApiResponse;
import com.alisondev.live_storage_hub.modules.apps.dtos.RegisterAppDTO;
import com.alisondev.live_storage_hub.modules.apps.dtos.AppResponseDTO;
import com.alisondev.live_storage_hub.modules.apps.entities.App;
import com.alisondev.live_storage_hub.modules.apps.services.RegisterAppService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/apps")
@Tag(name = "Apps", description = "Endpoints for apps.")
public class RegisterAppController {
  private final RegisterAppService registerAppService;

  public RegisterAppController(RegisterAppService registerAppService) {
    this.registerAppService = registerAppService;
  }

  @PostMapping("/register")
  @Operation(summary = "Register app", description = "Registers new apps.")
  public SendApiResponse<AppResponseDTO> handle(
      @RequestHeader("X-Admin-Key") String adminKey,
      @RequestBody RegisterAppDTO body) {
    App app = registerAppService.execute(adminKey, body.getName());
    return SendApiResponse.ok(toDto(app));
  }

  private AppResponseDTO toDto(App app) {
    AppResponseDTO dto = new AppResponseDTO();
    dto.setId(app.getId());
    dto.setName(app.getName());
    dto.setApiKey(app.getApiKey());
    dto.setCreatedAt(app.getCreatedAt());
    return dto;
  }
}
