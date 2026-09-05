package com.alisondev.live_storage_hub.modules.apps.controllers;

import com.alisondev.live_storage_hub.dtos.SendApiResponse;
import com.alisondev.live_storage_hub.modules.apps.dtos.AppResponseDTO;
import com.alisondev.live_storage_hub.dtos.ApiErrorResponseDTO;
import com.alisondev.live_storage_hub.modules.apps.entities.App;
import com.alisondev.live_storage_hub.modules.apps.services.ListAppsService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/apps")
@Tag(name = "Apps", description = "Endpoints for apps.")
public class ListAppsController {
  private final ListAppsService listAppsService;

  public ListAppsController(ListAppsService listAppsService) {
    this.listAppsService = listAppsService;
  }

  @GetMapping
  @Operation(summary = "List apps", description = "Lists all registered apps.")
  @ApiResponses({
      @ApiResponse(responseCode = "400", description = "Invalid admin key (2.2.1).",
          content = @Content(schema = @Schema(implementation = ApiErrorResponseDTO.class),
              examples = @ExampleObject(value = "{\"code\":\"2.2.1\",\"message\":\"Invalid admin key.\",\"data\":null}")))
  })
  public SendApiResponse<List<AppResponseDTO>> handle(
      @RequestHeader("X-Admin-Key") String adminKey) {
    List<AppResponseDTO> apps = listAppsService.execute(adminKey).stream()
        .map(this::toDto)
        .collect(Collectors.toList());
    return SendApiResponse.ok("Apps listed successfully.", apps);
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
