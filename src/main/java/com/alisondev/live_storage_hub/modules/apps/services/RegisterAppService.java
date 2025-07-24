package com.alisondev.live_storage_hub.modules.apps.services;

import com.alisondev.live_storage_hub.modules.apps.entities.App;
import com.alisondev.live_storage_hub.modules.apps.repositories.AppRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class RegisterAppService {
  private final AppRepository appRepository;

  @Value("${admin.api-key}")
  private String adminApiKey;

  public RegisterAppService(AppRepository appRepository) {
    this.appRepository = appRepository;
  }

  public App execute(String adminKey, String appName) {
    validateAdminKey(adminKey);

    App app = App.builder()
        .name(appName)
        .apiKey(UUID.randomUUID().toString())
        .build();

    return appRepository.save(app);
  }

  private void validateAdminKey(String adminKey) {
    if (!adminApiKey.equals(adminKey)) {
      throw new RuntimeException("[APP_ERROR]: Access denied, invalid admin key");
    }
  }
}
