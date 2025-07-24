package com.alisondev.live_storage_hub.modules.apps.controllers;

import com.alisondev.live_storage_hub.dtos.CustomApiResponse;
import com.alisondev.live_storage_hub.modules.apps.dtos.AppResponseDTO;
import com.alisondev.live_storage_hub.modules.apps.entities.App;
import com.alisondev.live_storage_hub.modules.apps.services.ListAppsService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/apps")
@Tag(name = "Apps", description = "Endpoints for managing apps.")
public class ListAppsController {
  private final ListAppsService listAppsService;

  public ListAppsController(ListAppsService listAppsService) {
    this.listAppsService = listAppsService;
  }

  @GetMapping
  @Operation(summary = "List apps", description = "Lists all registered apps.")
  public CustomApiResponse<List<AppResponseDTO>> handle(
      @RequestHeader("X-Admin-Key") String adminKey) {
    List<AppResponseDTO> apps = listAppsService.execute(adminKey).stream()
        .map(this::toDto)
        .collect(Collectors.toList());
    return CustomApiResponse.ok(apps);
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
