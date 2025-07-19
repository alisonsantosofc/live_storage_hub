package com.alisondev.live_storage_hub.controller;

import com.alisondev.live_storage_hub.dto.*;
import com.alisondev.live_storage_hub.entity.UserData;
import com.alisondev.live_storage_hub.security.JwtUtil;
import com.alisondev.live_storage_hub.service.UserDataService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/user_data")
@Tag(name = "User Data", description = "Endpoints para dados de usuários.")
public class UserDataController {
  private final UserDataService userDataService;
  private final JwtUtil jwtUtil;

  public UserDataController(UserDataService userDataService, JwtUtil jwtUtil) {
    this.userDataService = userDataService;
    this.jwtUtil = jwtUtil;
  }

  @Operation(summary = "Registro de dados do usuário", description = "Realiza registro de dados do usuário.")
  @PostMapping
  public ApiResponse<UserDataResponse> saveData(@RequestHeader("Authorization") String authHeader,
      @RequestParam("userId") Long userId,
      @RequestBody UserDataRequest request) {
    String token = authHeader.substring(7);
    Long appId = jwtUtil.getAppIdFromToken(token);
    UserData data = userDataService.saveData(appId, userId, request);
    return ApiResponse.ok(toDto(data));
  }

  @Operation(summary = "Listagem de dados do usuário", description = "Lista todos os dados do usuário.")
  @GetMapping
  public ApiResponse<List<UserDataResponse>> listData(@RequestHeader("Authorization") String authHeader,
      @RequestParam("userId") Long userId) {
    String token = authHeader.substring(7);
    Long appId = jwtUtil.getAppIdFromToken(token);
    List<UserDataResponse> list = userDataService.listData(appId, userId)
        .stream().map(this::toDto).collect(Collectors.toList());
    return ApiResponse.ok(list);
  }

  private UserDataResponse toDto(UserData data) {
    UserDataResponse dto = new UserDataResponse();
    dto.setId(data.getId());
    dto.setJsonData(data.getJsonData());
    dto.setCreatedAt(data.getCreatedAt());
    return dto;
  }
}
