package com.alisondev.live_storage_hub.dto.user_data;

import java.util.Map;

import lombok.Data;

@Data
public class UserDataRequest {
  private Map<String, Object> jsonData;
}
