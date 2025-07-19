package com.alisondev.live_storage_hub.controller;

import com.alisondev.live_storage_hub.dto.ApiResponse;
import com.alisondev.live_storage_hub.dto.AppResponse;
import com.alisondev.live_storage_hub.entity.App;
import com.alisondev.live_storage_hub.service.AppService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/apps")
public class AppController {

  private final AppService appService;

  public AppController(AppService appService) {
    this.appService = appService;
  }

  @PostMapping("/register")
  public ApiResponse<AppResponse> registerApp(@RequestHeader("X-Admin-Key") String adminKey,
      @RequestBody Map<String, String> body) {
    App app = appService.registerApp(adminKey, body.get("name"));
    return ApiResponse.ok(toDto(app));
  }

  @GetMapping
  public ApiResponse<List<AppResponse>> listApps(@RequestHeader("X-Admin-Key") String adminKey) {
    List<AppResponse> apps = appService.listApps(adminKey).stream()
        .map(this::toDto)
        .collect(Collectors.toList());
    return ApiResponse.ok(apps);
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
