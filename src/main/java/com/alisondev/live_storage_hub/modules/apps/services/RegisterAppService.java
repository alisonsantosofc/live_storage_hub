package com.alisondev.live_storage_hub.modules.apps.services;

import com.alisondev.live_storage_hub.modules.apps.entities.App;
import com.alisondev.live_storage_hub.modules.apps.repositories.AppRepository;
import com.alisondev.live_storage_hub.modules.apps.errors.AppsErrorPrefix;
import com.alisondev.live_storage_hub.exceptions.ApiRuntimeException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class RegisterAppService {
  private final AppRepository appRepository;

  @Value("${admin.api-key}")
  private String adminApiKey;

  private final String prefix = AppsErrorPrefix.MODULE + "." + AppsErrorPrefix.ROUTE_REGISTER_APP + ".";

  public RegisterAppService(AppRepository appRepository) {
    this.appRepository = appRepository;
  }

  public App execute(String adminKey, String appName) {
    if (!adminApiKey.equals(adminKey)) {
      throw new ApiRuntimeException(prefix + 1, "Access denied, invalid admin key.");
    }

    App app = App.builder()
        .name(appName)
        .apiKey(UUID.randomUUID().toString())
        .build();

    return appRepository.save(app);
  }
}
