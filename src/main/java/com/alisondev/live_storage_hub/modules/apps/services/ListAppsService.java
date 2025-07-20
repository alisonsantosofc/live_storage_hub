package com.alisondev.live_storage_hub.modules.apps.services;

import com.alisondev.live_storage_hub.modules.apps.entities.App;
import com.alisondev.live_storage_hub.modules.apps.repositories.AppRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ListAppsService {
  private final AppRepository appRepository;

  @Value("${admin.api-key}")
  private String adminApiKey;

  public ListAppsService(AppRepository appRepository) {
    this.appRepository = appRepository;
  }

  public List<App> execute(String adminKey) {
    validateAdminKey(adminKey);
    return appRepository.findAll();
  }

  private void validateAdminKey(String adminKey) {
    if (!adminApiKey.equals(adminKey)) {
      throw new RuntimeException("Acesso negado: admin key inválida");
    }
  }
}
