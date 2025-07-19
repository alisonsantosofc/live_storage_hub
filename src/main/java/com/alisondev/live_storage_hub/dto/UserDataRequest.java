package com.alisondev.live_storage_hub.dto;

import java.util.Map;

import lombok.Data;

@Data
public class UserDataRequest {
  private Map<String, Object> jsonData;
}
