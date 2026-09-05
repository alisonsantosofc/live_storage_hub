package com.alisondev.live_storage_hub.modules.users.services;

import com.alisondev.live_storage_hub.config.StorageConfig;
import com.alisondev.live_storage_hub.exceptions.ApiRuntimeException;
import com.alisondev.live_storage_hub.modules.apps.entities.App;
import com.alisondev.live_storage_hub.modules.apps.repositories.AppRepository;
import com.alisondev.live_storage_hub.modules.users.entities.User;
import com.alisondev.live_storage_hub.modules.users.entities.UserFile;
import com.alisondev.live_storage_hub.modules.users.repositories.UserFileRepository;
import com.alisondev.live_storage_hub.modules.users.repositories.UserRepository;
import com.alisondev.live_storage_hub.modules.users.errors.UsersErrorPrefix;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class UserFileStorageService {
  private static final String PREFIX = UsersErrorPrefix.MODULE + "."
      + UsersErrorPrefix.ROUTE_USER_FILE_CONTENT + ".";
  private final AppRepository appRepository;
  private final UserRepository userRepository;
  private final UserFileRepository userFileRepository;
  private final StorageConfig storageConfig;
  private final S3Client s3Client;

  public UserFileStorageService(AppRepository appRepository, UserRepository userRepository,
      UserFileRepository userFileRepository, StorageConfig storageConfig,
      @Autowired(required = false) S3Client s3Client) {
    this.appRepository = appRepository;
    this.userRepository = userRepository;
    this.userFileRepository = userFileRepository;
    this.storageConfig = storageConfig;
    this.s3Client = s3Client;
  }

  public StoredFile download(Long appId, Long userId, Long fileId) throws IOException {
    UserFile userFile = findOwnedFile(appId, userId, fileId);
    byte[] content;

    if ("local".equalsIgnoreCase(storageConfig.getStorageMode())) {
      Path root = Paths.get(storageConfig.getLocalPath()).toAbsolutePath().normalize();
      Path path = root.resolve(userFile.getFileUrl()).normalize();
      if (!path.startsWith(root) || !Files.isRegularFile(path))
        throw new ApiRuntimeException(PREFIX + 5, "Stored file was not found.");
      content = Files.readAllBytes(path);
    } else if ("s3".equalsIgnoreCase(storageConfig.getStorageMode()) && s3Client != null) {
      content = s3Client.getObjectAsBytes(GetObjectRequest.builder()
          .bucket(storageConfig.getBucketName()).key(userFile.getFileUrl()).build()).asByteArray();
    } else {
      throw new ApiRuntimeException(PREFIX + 6, "File storage is not configured.");
    }

    Object name = userFile.getMetadata() == null ? null : userFile.getMetadata().get("name");
    Object contentType = userFile.getMetadata() == null ? null : userFile.getMetadata().get("contentType");
    return new StoredFile(content,
        name == null ? "file" : name.toString(),
        contentType == null ? "application/octet-stream" : contentType.toString());
  }

  public void delete(Long appId, Long userId, Long fileId) throws IOException {
    UserFile userFile = findOwnedFile(appId, userId, fileId);

    if ("local".equalsIgnoreCase(storageConfig.getStorageMode())) {
      Path root = Paths.get(storageConfig.getLocalPath()).toAbsolutePath().normalize();
      Path path = root.resolve(userFile.getFileUrl()).normalize();
      if (!path.startsWith(root))
        throw new ApiRuntimeException(PREFIX + 7, "Invalid stored file path.");
      Files.deleteIfExists(path);
    } else if ("s3".equalsIgnoreCase(storageConfig.getStorageMode()) && s3Client != null) {
      s3Client.deleteObject(DeleteObjectRequest.builder()
          .bucket(storageConfig.getBucketName()).key(userFile.getFileUrl()).build());
    } else {
      throw new ApiRuntimeException(PREFIX + 6, "File storage is not configured.");
    }

    userFileRepository.delete(userFile);
  }

  private UserFile findOwnedFile(Long appId, Long userId, Long fileId) {
    App app = appRepository.findById(appId)
        .orElseThrow(() -> new ApiRuntimeException(PREFIX + 1, "App not found."));
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new ApiRuntimeException(PREFIX + 2, "User not found."));
    if (!user.getApp().getId().equals(appId))
      throw new ApiRuntimeException(PREFIX + 3, "User does not belong to this app.");
    return userFileRepository.findByAppAndUserAndId(app, user, fileId)
        .orElseThrow(() -> new ApiRuntimeException(PREFIX + 4, "File not found."));
  }

  public record StoredFile(byte[] content, String originalName, String contentType) {}
}
