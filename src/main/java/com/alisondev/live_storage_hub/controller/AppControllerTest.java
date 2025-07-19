package com.alisondev.live_storage_hub.controller;

import com.alisondev.live_storage_hub.entity.App;
import com.alisondev.live_storage_hub.service.AppService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AppController.class)
class AppControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private AppService appService;

  @Test
  void listApps_shouldReturnOk() throws Exception {
    App app = new App(1L, "TestApp", "12345", LocalDateTime.now());
    Mockito.when(appService.listApps("admin-key")).thenReturn(List.of(app));

    mockMvc.perform(get("/apps")
        .header("X-Admin-Key", "admin-key"))
        .andExpect(status().isOk());
  }
}
