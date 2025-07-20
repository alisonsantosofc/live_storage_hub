package com.alisondev.live_storage_hub.modules.users.dtos.user_data;

import java.util.Map;

import lombok.Data;

@Data
public class UserDataRequest {
  private Map<String, Object> jsonData;
}
