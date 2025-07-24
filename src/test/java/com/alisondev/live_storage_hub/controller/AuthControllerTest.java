package com.alisondev.live_storage_hub.controller;

import com.alisondev.live_storage_hub.modules.users.controllers.AuthController;
import com.alisondev.live_storage_hub.modules.users.dtos.AuthResponseDTO;
import com.alisondev.live_storage_hub.modules.users.dtos.LoginUserDTO;
import com.alisondev.live_storage_hub.modules.users.services.AuthService;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthController.class)
class AuthControllerTest {
  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private AuthService authService;

  @Autowired
  private ObjectMapper objectMapper;

  @Test
  void login_shouldReturnOk() throws Exception {
    LoginUserDTO loginUserDTO = new LoginUserDTO();
    loginUserDTO.setEmail("user@test.com");
    loginUserDTO.setPassword("123456");

    AuthResponseDTO response = new AuthResponseDTO();
    response.setToken("fake-jwt-token");

    Mockito.when(authService.login(Mockito.eq("apikey123"), Mockito.any(LoginUserDTO.class)))
        .thenReturn(response);

    mockMvc.perform(post("/auth/login")
        .header("X-API-KEY", "apikey123")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(loginUserDTO)))
        .andExpect(status().isOk());
  }
}
