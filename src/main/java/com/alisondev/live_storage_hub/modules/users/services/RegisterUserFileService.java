package com.alisondev.live_storage_hub.modules.users.services;

import com.alisondev.live_storage_hub.config.StorageConfig;
import com.alisondev.live_storage_hub.modules.apps.entities.App;
import com.alisondev.live_storage_hub.modules.users.entities.User;
import com.alisondev.live_storage_hub.modules.users.entities.UserFile;
import com.alisondev.live_storage_hub.modules.apps.repositories.AppRepository;
import com.alisondev.live_storage_hub.modules.users.repositories.UserRepository;

import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import com.alisondev.live_storage_hub.modules.users.repositories.UserFileRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Map;
import java.util.UUID;

@Service
public class RegisterUserFileService {
  private final AppRepository appRepository;
  private final UserRepository userRepository;
  private final UserFileRepository userFileRepository;
  private final StorageConfig storageConfig;
  private final S3Client s3Client;

  public RegisterUserFileService(AppRepository appRepository, UserRepository userRepository,
      UserFileRepository userFileRepository, StorageConfig storageConfig,
      @Autowired(required = false) S3Client s3Client) {
    this.appRepository = appRepository;
    this.userRepository = userRepository;
    this.userFileRepository = userFileRepository;
    this.storageConfig = storageConfig;
    this.s3Client = s3Client;
  }

  public UserFile execute(Long appId, Long userId, MultipartFile file, String fileType) throws IOException {
    App app = appRepository.findById(appId)
        .orElseThrow(() -> new RuntimeException("App não encontrado"));
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

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
    } else {
      throw new RuntimeException("Configuração de storage inválida");
    } 

    UserFile userFile = UserFile.builder()
        .app(app)
        .user(user)
        .fileType(fileType)
        .fileUrl(fileUrl)
        .metadata(Map.of("size", file.getSize(), "name", file.getOriginalFilename()))
        .build();

    return userFileRepository.save(userFile);
  }
}
