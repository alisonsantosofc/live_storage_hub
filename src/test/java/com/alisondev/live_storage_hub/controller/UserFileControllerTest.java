package com.alisondev.live_storage_hub.controller;

import com.alisondev.live_storage_hub.config.StorageConfig;
import com.alisondev.live_storage_hub.modules.apps.entities.App;
import com.alisondev.live_storage_hub.modules.apps.repositories.AppRepository;
import com.alisondev.live_storage_hub.modules.users.entities.User;
import com.alisondev.live_storage_hub.modules.users.entities.UserFile;
import com.alisondev.live_storage_hub.modules.users.repositories.UserFileRepository;
import com.alisondev.live_storage_hub.modules.users.repositories.UserRepository;
import com.alisondev.live_storage_hub.modules.users.services.RegisterUserFileService;
import com.alisondev.live_storage_hub.modules.users.services.UserFileStorageService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserFileControllerTest {
  @Mock private AppRepository appRepository;
  @Mock private UserRepository userRepository;
  @Mock private UserFileRepository userFileRepository;
  @Mock private StorageConfig storageConfig;
  @TempDir Path storageRoot;

  private App app;
  private User user;

  @BeforeEach
  void setUp() {
    app = App.builder().id(1L).build();
    user = User.builder().id(2L).app(app).build();
    when(storageConfig.getStorageMode()).thenReturn("local");
    when(storageConfig.getLocalPath()).thenReturn(storageRoot.toString());
  }

  @Test
  void uploadStoresOpaqueKeyAndSafeMetadata() throws Exception {
    when(appRepository.findById(1L)).thenReturn(Optional.of(app));
    when(userRepository.findById(2L)).thenReturn(Optional.of(user));
    when(userFileRepository.save(any(UserFile.class)))
        .thenAnswer(invocation -> invocation.getArgument(0));
    RegisterUserFileService service = new RegisterUserFileService(
        appRepository, userRepository, userFileRepository, storageConfig, null);
    MockMultipartFile upload = new MockMultipartFile(
        "file", "../../unsafe.txt", "text/plain", "content".getBytes());

    UserFile saved = service.execute(1L, 2L, upload, "document");

    assertTrue(saved.getFileUrl().matches("apps/1/users/2/[0-9a-f-]{36}"));
    assertEquals("../../unsafe.txt", saved.getMetadata().get("name"));
    assertEquals("text/plain", saved.getMetadata().get("contentType"));
    assertTrue(Files.isRegularFile(storageRoot.resolve(saved.getFileUrl())));
  }

  @Test
  void downloadReturnsStoredBytesAndMetadata() throws Exception {
    Path storedPath = storageRoot.resolve("apps/1/users/2/file-id");
    Files.createDirectories(storedPath.getParent());
    Files.write(storedPath, "content".getBytes());
    UserFile file = ownedFile("apps/1/users/2/file-id");
    when(appRepository.findById(1L)).thenReturn(Optional.of(app));
    when(userRepository.findById(2L)).thenReturn(Optional.of(user));
    when(userFileRepository.findByAppAndUserAndId(app, user, 3L)).thenReturn(Optional.of(file));
    UserFileStorageService service = new UserFileStorageService(
        appRepository, userRepository, userFileRepository, storageConfig, null);

    UserFileStorageService.StoredFile result = service.download(1L, 2L, 3L);

    assertArrayEquals("content".getBytes(), result.content());
    assertEquals("report.txt", result.originalName());
    assertEquals("text/plain", result.contentType());
  }

  @Test
  void deleteRemovesContentBeforeDatabaseRecord() throws Exception {
    Path storedPath = storageRoot.resolve("apps/1/users/2/file-id");
    Files.createDirectories(storedPath.getParent());
    Files.write(storedPath, "content".getBytes());
    UserFile file = ownedFile("apps/1/users/2/file-id");
    when(appRepository.findById(1L)).thenReturn(Optional.of(app));
    when(userRepository.findById(2L)).thenReturn(Optional.of(user));
    when(userFileRepository.findByAppAndUserAndId(app, user, 3L)).thenReturn(Optional.of(file));
    UserFileStorageService service = new UserFileStorageService(
        appRepository, userRepository, userFileRepository, storageConfig, null);

    service.delete(1L, 2L, 3L);

    assertFalse(Files.exists(storedPath));
    verify(userFileRepository).delete(file);
  }

  private UserFile ownedFile(String key) {
    return UserFile.builder()
        .id(3L)
        .app(app)
        .user(user)
        .fileUrl(key)
        .metadata(java.util.Map.of("name", "report.txt", "contentType", "text/plain"))
        .build();
  }
}
