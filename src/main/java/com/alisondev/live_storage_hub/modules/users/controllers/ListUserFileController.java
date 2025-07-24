package com.alisondev.live_storage_hub.modules.users.controllers;

import com.alisondev.live_storage_hub.modules.apps.entities.App;
import com.alisondev.live_storage_hub.modules.users.entities.User;
import com.alisondev.live_storage_hub.modules.users.entities.UserFile;
import com.alisondev.live_storage_hub.modules.apps.repositories.AppRepository;
import com.alisondev.live_storage_hub.modules.users.repositories.UserRepository;
import com.alisondev.live_storage_hub.modules.users.repositories.UserFileRepository;
import com.alisondev.live_storage_hub.modules.users.dtos.UserFileResponseDTO;
import com.alisondev.live_storage_hub.dtos.CustomApiResponse;
import com.alisondev.live_storage_hub.security.JwtUtil;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/user_file")
@Tag(name = "User File", description = "Endpoints para arquivos de usuários.")
public class ListUserFileController {
  private final AppRepository appRepository;
  private final UserRepository userRepository;
  private final UserFileRepository userFileRepository;
  private final JwtUtil jwtUtil;

  @Autowired
  public ListUserFileController(AppRepository appRepository, UserRepository userRepository,
      UserFileRepository userFileRepository, JwtUtil jwtUtil) {
    this.appRepository = appRepository;
    this.userRepository = userRepository;
    this.userFileRepository = userFileRepository;
    this.jwtUtil = jwtUtil;
  }

  @Operation(summary = "Listagem de arquivos do usuário", description = "Lista todos os arquivos do usuário.")
  @GetMapping("/list")
  public CustomApiResponse<List<UserFileResponseDTO>> handle(@RequestHeader("Authorization") String authHeader,
      @RequestParam Long userId) {
    String token = authHeader.substring(7);
    Long appId = jwtUtil.getAppIdFromToken(token);

    App app = appRepository.findById(appId).orElseThrow(() -> new RuntimeException("App não encontrado"));
    User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

    if (!user.getApp().getId().equals(appId))
      throw new RuntimeException("Usuário não pertence a este App");

    List<UserFileResponseDTO> responseList = userFileRepository.findByAppAndUser(app, user)
        .stream().map(this::toDto)
        .collect(Collectors.toList());

    return CustomApiResponse.ok(responseList);
  }

  private UserFileResponseDTO toDto(UserFile entity) {
    UserFileResponseDTO dto = new UserFileResponseDTO();
    dto.setId(entity.getId());
    dto.setFileType(entity.getFileType());
    dto.setFileUrl(entity.getFileUrl());
    dto.setMetadata(entity.getMetadata());
    dto.setCreatedAt(entity.getCreatedAt());
    return dto;
  }
}
