package com.alisondev.live_storage_hub.modules.apps.controllers;

import com.alisondev.live_storage_hub.dtos.CustomApiResponse;
import com.alisondev.live_storage_hub.modules.apps.dtos.AppRegisterRequest;
import com.alisondev.live_storage_hub.modules.apps.dtos.AppResponse;
import com.alisondev.live_storage_hub.modules.apps.entities.App;
import com.alisondev.live_storage_hub.modules.apps.services.AppService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/apps")
@Tag(name = "Apps", description = "Endpoints para aplicativos.")
public class AppController {
  private final AppService appService;

  public AppController(AppService appService) {
    this.appService = appService;
  }

  @PostMapping("/register")
  @Operation(summary = "Registro de apps", description = "Realiza registro de app e retorna dados do app.")
  @ApiResponse(responseCode = "200", description = "Aplicativo registrado com sucesso")
  public CustomApiResponse<AppResponse> registerApp(@RequestHeader("X-Admin-Key") String adminKey,
      @RequestBody AppRegisterRequest body) {
    App app = appService.registerApp(adminKey, body.getName());
    return CustomApiResponse.ok(toDto(app));
  }

  @GetMapping
  @Operation(summary = "Listagem de apps", description = "Lista todos os apps registrados.")
  public CustomApiResponse<List<AppResponse>> listApps(@RequestHeader("X-Admin-Key") String adminKey) {
    List<AppResponse> apps = appService.listApps(adminKey).stream()
        .map(this::toDto)
        .collect(Collectors.toList());
    return CustomApiResponse.ok(apps);
  }

  private AppResponse toDto(App app) {
    AppResponse dto = new AppResponse();
    dto.setId(app.getId());
    dto.setName(app.getName());
    dto.setApiKey(app.getApiKey());
    dto.setCreatedAt(app.getCreatedAt());
    return dto;
  }
}
