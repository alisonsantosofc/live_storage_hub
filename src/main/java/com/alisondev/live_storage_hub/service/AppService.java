package com.alisondev.live_storage_hub.service;

import com.alisondev.live_storage_hub.entity.App;
import com.alisondev.live_storage_hub.repository.AppRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class AppService {
  private final AppRepository appRepository;

  @Value("${admin.api-key}")
  private String adminApiKey;

  public AppService(AppRepository appRepository) {
    this.appRepository = appRepository;
  }

  public App registerApp(String adminKey, String appName) {
    if (!adminApiKey.equals(adminKey)) {
      throw new RuntimeException("Acesso negado: admin key inválida");
    }

    App app = App.builder()
        .name(appName)
        .apiKey(UUID.randomUUID().toString())
        .build();

    return appRepository.save(app);
  }

  public List<App> listApps(String adminKey) {
    if (!adminApiKey.equals(adminKey)) {
      throw new RuntimeException("Acesso negado: admin key inválida");
    }
    return appRepository.findAll();
  }
}
