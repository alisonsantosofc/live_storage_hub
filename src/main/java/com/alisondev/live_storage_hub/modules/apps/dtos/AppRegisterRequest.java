package com.alisondev.live_storage_hub.modules.apps.dtos;

import io.swagger.v3.oas.annotations.media.Schema;

public class AppRegisterRequest {
  @Schema(description = "App name for register.", example = "MeuApp")
  private String name;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }
}