package com.alisondev.live_storage_hub.controller;

import com.alisondev.live_storage_hub.config.StorageConfig;
import com.alisondev.live_storage_hub.modules.apps.entities.App;
import com.alisondev.live_storage_hub.modules.apps.repositories.AppRepository;
import com.alisondev.live_storage_hub.modules.users.controllers.UserFileController;
import com.alisondev.live_storage_hub.modules.users.entities.User;
import com.alisondev.live_storage_hub.modules.users.entities.UserFile;
import com.alisondev.live_storage_hub.modules.users.repositories.UserFileRepository;
import com.alisondev.live_storage_hub.modules.users.repositories.UserRepository;
import com.alisondev.live_storage_hub.security.JwtUtil;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserFileController.class)
class UserFileControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private AppRepository appRepository;

  @MockBean
  private UserRepository userRepository;

  @MockBean
  private UserFileRepository userFileRepository;

  @MockBean
  private JwtUtil jwtUtil;

  @MockBean
  private StorageConfig storageConfig;

  @Test
  void uploadFile_shouldReturnOk() throws Exception {
    MockMultipartFile file = new MockMultipartFile("file", "test.txt",
        "text/plain", "Hello".getBytes());

    App app = App.builder().id(1L).name("TestApp").build();
    User user = User.builder().id(1L).name("John").app(app).build();

    Mockito.when(jwtUtil.getAppIdFromToken("fake-token")).thenReturn(1L);
    Mockito.when(appRepository.findById(1L)).thenReturn(java.util.Optional.of(app));
    Mockito.when(userRepository.findById(1L)).thenReturn(java.util.Optional.of(user));
    Mockito.when(storageConfig.getStorageMode()).thenReturn("local");
    Mockito.when(storageConfig.getLocalPath()).thenReturn("./tmp/uploads");

    Path tmpDir = Path.of("./tmp/uploads");
    if (!Files.exists(tmpDir))
      Files.createDirectories(tmpDir);

    mockMvc.perform(multipart("/user_file/upload")
        .file(file)
        .param("userId", "1")
        .param("fileType", "image")
        .header("Authorization", "Bearer fake-token"))
        .andExpect(status().isOk());
  }

  @Test
  void listFiles_shouldReturnOk() throws Exception {
    App app = App.builder().id(1L).name("TestApp").build();
    User user = User.builder().id(1L).name("John").app(app).build();
    UserFile userFile = UserFile.builder()
        .id(1L)
        .fileType("image")
        .fileUrl("http://localhost/test.txt")
        .metadata(Map.of("size", 10, "name", "test.txt"))
        .build();

    Mockito.when(jwtUtil.getAppIdFromToken("fake-token")).thenReturn(1L);
    Mockito.when(appRepository.findById(1L)).thenReturn(java.util.Optional.of(app));
    Mockito.when(userRepository.findById(1L)).thenReturn(java.util.Optional.of(user));
    Mockito.when(userFileRepository.findByAppAndUser(app, user)).thenReturn(List.of(userFile));

    mockMvc.perform(get("/user_file/list")
        .header("Authorization", "Bearer fake-token")
        .param("userId", "1"))
        .andExpect(status().isOk());
  }
}
