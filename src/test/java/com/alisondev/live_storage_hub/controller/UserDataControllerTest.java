package com.alisondev.live_storage_hub.controller;

import com.alisondev.live_storage_hub.dtos.user_data.UserDataRequest;
import com.alisondev.live_storage_hub.modules.apps.entities.App;
import com.alisondev.live_storage_hub.modules.users.controllers.UserDataController;
import com.alisondev.live_storage_hub.modules.users.entities.User;
import com.alisondev.live_storage_hub.modules.users.entities.UserData;
import com.alisondev.live_storage_hub.modules.users.services.UserDataService;
import com.alisondev.live_storage_hub.security.JwtUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserDataController.class)
class UserDataControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private UserDataService userDataService;

  @MockBean
  private JwtUtil jwtUtil;

  @Autowired
  private ObjectMapper objectMapper;

  @Test
  void createUserData_shouldReturnOk() throws Exception {
    Long appId = 1L;
    Long userId = 10L;
    Map<String, Object> json = Map.of("level", "easy");

    UserDataRequest request = new UserDataRequest();
    request.setJsonData(json);

    App app = App.builder().id(appId).name("TestApp").apiKey("abc").createdAt(LocalDateTime.now()).build();
    User user = User.builder().id(userId).name("Test User").email("test@example.com").app(app)
        .createdAt(LocalDateTime.now()).build();

    UserData userData = UserData.builder()
        .id(99L)
        .app(app)
        .user(user)
        .jsonData(json)
        .createdAt(LocalDateTime.now())
        .build();

    Mockito.when(jwtUtil.getAppIdFromToken("mock-token")).thenReturn(appId);
    Mockito.when(userDataService.saveData(Mockito.eq(appId), Mockito.eq(userId), Mockito.any(UserDataRequest.class)))
        .thenReturn(userData);

    mockMvc.perform(post("/userdata")
        .header("Authorization", "Bearer mock-token")
        .param("userId", userId.toString())
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(json)))
        .andExpect(status().isOk());
  }

  @Test
  void listUserData_shouldReturnOk() throws Exception {
    Long appId = 1L;
    Long userId = 10L;

    App app = App.builder().id(appId).name("TestApp").apiKey("abc").createdAt(LocalDateTime.now()).build();
    User user = User.builder().id(userId).name("Test User").email("test@example.com").app(app)
        .createdAt(LocalDateTime.now()).build();

    UserData userData = UserData.builder()
        .id(1L)
        .app(app)
        .user(user)
        .jsonData(Map.of("score", 99))
        .createdAt(LocalDateTime.now())
        .build();

    Mockito.when(jwtUtil.getAppIdFromToken("mock-token")).thenReturn(appId);
    Mockito.when(userDataService.listData(appId, userId))
        .thenReturn(List.of(userData));

    mockMvc.perform(get("/userdata")
        .header("Authorization", "Bearer mock-token")
        .param("userId", userId.toString()))
        .andExpect(status().isOk());
  }
}
