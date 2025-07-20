package com.alisondev.live_storage_hub.modules.users.controllers;

import com.alisondev.live_storage_hub.config.StorageConfig;
import com.alisondev.live_storage_hub.dtos.CustomApiResponse;
import com.alisondev.live_storage_hub.dtos.user_file.UserFileResponse;
import com.alisondev.live_storage_hub.modules.apps.entities.App;
import com.alisondev.live_storage_hub.modules.apps.repositories.AppRepository;
import com.alisondev.live_storage_hub.modules.users.entities.User;
import com.alisondev.live_storage_hub.modules.users.entities.UserFile;
import com.alisondev.live_storage_hub.modules.users.repositories.UserFileRepository;
import com.alisondev.live_storage_hub.modules.users.repositories.UserRepository;
import com.alisondev.live_storage_hub.security.JwtUtil;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.nio.file.*;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/user_file")
@Tag(name = "User File", description = "Endpoints para arquivos de usuários.")
public class UserFileController {
  private final AppRepository appRepository;
  private final UserRepository userRepository;
  private final UserFileRepository userFileRepository;
  private final JwtUtil jwtUtil;
  private final StorageConfig storageConfig;
  private final S3Client s3Client;

  @Autowired
  public UserFileController(AppRepository appRepository, UserRepository userRepository,
      UserFileRepository userFileRepository, JwtUtil jwtUtil,
      StorageConfig storageConfig, @Autowired(required = false) S3Client s3Client) {
    this.appRepository = appRepository;
    this.userRepository = userRepository;
    this.userFileRepository = userFileRepository;
    this.jwtUtil = jwtUtil;
    this.storageConfig = storageConfig;
    this.s3Client = s3Client;
  }

  @Operation(summary = "Upload de arquivos do usuário", description = "Realiza upload de arquivos do usuário.")
  @PostMapping("/upload")
  public CustomApiResponse<UserFileResponse> uploadFile(@RequestHeader("Authorization") String authHeader,
      @RequestParam("userId") Long userId,
      @RequestParam("file") MultipartFile file,
      @RequestParam("fileType") String fileType) throws IOException {
    String token = authHeader.substring(7);
    Long appId = jwtUtil.getAppIdFromToken(token);

    App app = appRepository.findById(appId).orElseThrow(() -> new RuntimeException("App não encontrado"));
    User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

    if (!user.getApp().getId().equals(appId))
      throw new RuntimeException("Usuário não pertence a este App");

    String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
    String fileUrl;

    if ("local".equalsIgnoreCase(storageConfig.getStorageMode())) {
      Path uploadPath = Paths.get(storageConfig.getLocalPath());

      if (!Files.exists(uploadPath))
        Files.createDirectories(uploadPath);

      Path destination = uploadPath.resolve(fileName);

      Files.copy(file.getInputStream(), destination, StandardCopyOption.REPLACE_EXISTING);
      fileUrl = destination.toAbsolutePath().toString();
    } else if ("s3".equalsIgnoreCase(storageConfig.getStorageMode())) {
      if (s3Client == null)
        throw new RuntimeException("S3 não está configurado");

      s3Client.putObject(
          PutObjectRequest.builder()
              .bucket(storageConfig.getBucketName())
              .key(fileName)
              .build(),
          software.amazon.awssdk.core.sync.RequestBody.fromBytes(file.getBytes()));

      fileUrl = "https://" + storageConfig.getBucketName() + ".s3.amazonaws.com/" + fileName;
    } else
      throw new RuntimeException("Configuração de storage inválida");

    UserFile userFile = UserFile.builder()
        .app(app)
        .user(user)
        .fileType(fileType)
        .fileUrl(fileUrl)
        .metadata(Map.of("size", file.getSize(), "name", file.getOriginalFilename()))
        .build();

    userFileRepository.save(userFile);

    return CustomApiResponse.ok(toDto(userFile));
  }

  @Operation(summary = "Listagem de arquivos do usuário", description = "Lista todos os arquivos do usuário.")
  @GetMapping("/list")
  public CustomApiResponse<List<UserFileResponse>> listFiles(@RequestHeader("Authorization") String authHeader,
      @RequestParam Long userId) {
    String token = authHeader.substring(7);
    Long appId = jwtUtil.getAppIdFromToken(token);

    App app = appRepository.findById(appId).orElseThrow(() -> new RuntimeException("App não encontrado"));
    User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

    if (!user.getApp().getId().equals(appId))
      throw new RuntimeException("Usuário não pertence a este App");

    List<UserFileResponse> responseList = userFileRepository.findByAppAndUser(app, user)
        .stream().map(this::toDto)
        .collect(Collectors.toList());

    return CustomApiResponse.ok(responseList);
  }

  private UserFileResponse toDto(UserFile entity) {
    UserFileResponse dto = new UserFileResponse();
    dto.setId(entity.getId());
    dto.setFileType(entity.getFileType());
    dto.setFileUrl(entity.getFileUrl());
    dto.setMetadata(entity.getMetadata());
    dto.setCreatedAt(entity.getCreatedAt());
    return dto;
  }
}
