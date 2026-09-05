package com.alisondev.live_storage_hub.modules.users.services;

import com.alisondev.live_storage_hub.modules.apps.entities.App;
import com.alisondev.live_storage_hub.modules.users.entities.User;
import com.alisondev.live_storage_hub.modules.users.entities.UserFile;
import com.alisondev.live_storage_hub.modules.apps.repositories.AppRepository;
import com.alisondev.live_storage_hub.modules.users.repositories.UserRepository;
import com.alisondev.live_storage_hub.modules.users.errors.UsersErrorPrefix;
import com.alisondev.live_storage_hub.exceptions.ApiRuntimeException;
import com.alisondev.live_storage_hub.config.StorageConfig;

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
import java.nio.file.InvalidPathException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class RegisterUserFileService {
  private final AppRepository appRepository;
  private final UserRepository userRepository;
  private final UserFileRepository userFileRepository;
  private final StorageConfig storageConfig;
  private final S3Client s3Client;
  private final String prefix = UsersErrorPrefix.MODULE + "." + UsersErrorPrefix.ROUTE_REGISTER_USER_FILE + ".";

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
    if (file.isEmpty())
      throw new ApiRuntimeException(prefix + 6, "File cannot be empty.");

    App app = appRepository.findById(appId)
        .orElseThrow(() -> new ApiRuntimeException(prefix + 1, "App not found or invalid api key."));
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new ApiRuntimeException(prefix + 2, "User not found or invalid user id."));

    if (!user.getApp().getId().equals(appId))
      throw new ApiRuntimeException(prefix + 3, "User does not registered to this app.");

    String fileName = UUID.randomUUID().toString();
    String storageKey = "apps/" + appId + "/users/" + userId + "/" + fileName;
    String fileUrl;

    if ("local".equalsIgnoreCase(storageConfig.getStorageMode())) {
      Path uploadPath = Paths.get(storageConfig.getLocalPath()).toAbsolutePath().normalize();

      if (!Files.exists(uploadPath))
        Files.createDirectories(uploadPath);

      Path destination = uploadPath.resolve(storageKey).normalize();
      if (!destination.startsWith(uploadPath))
        throw new InvalidPathException(storageKey, "Invalid storage path");
      Files.createDirectories(destination.getParent());

      Files.copy(file.getInputStream(), destination, StandardCopyOption.REPLACE_EXISTING);
      fileUrl = storageKey;
    } else if ("s3".equalsIgnoreCase(storageConfig.getStorageMode())) {
      if (s3Client == null)
        throw new ApiRuntimeException(prefix + 4, "S3 is not configured.");

      s3Client.putObject(
          PutObjectRequest.builder()
              .bucket(storageConfig.getBucketName())
              .key(storageKey)
              .build(),
          software.amazon.awssdk.core.sync.RequestBody.fromBytes(file.getBytes()));

      fileUrl = storageKey;
    } else {
      throw new ApiRuntimeException(prefix + 5, "Invalid storage config.");
    } 

    Map<String, Object> metadata = new HashMap<>();
    metadata.put("size", file.getSize());
    metadata.put("name", file.getOriginalFilename() == null ? "file" : file.getOriginalFilename());
    metadata.put("contentType", file.getContentType() == null ? "application/octet-stream" : file.getContentType());

    UserFile userFile = UserFile.builder()
        .app(app)
        .user(user)
        .fileType(fileType)
        .fileUrl(fileUrl)
        .metadata(metadata)
        .build();

    return userFileRepository.save(userFile);
  }
}
