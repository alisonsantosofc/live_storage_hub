package com.alisondev.live_storage_hub.modules.apps.services;

import com.alisondev.live_storage_hub.modules.apps.entities.App;
import com.alisondev.live_storage_hub.modules.apps.repositories.AppRepository;
import com.alisondev.live_storage_hub.modules.apps.errors.AppsErrorPrefix;
import com.alisondev.live_storage_hub.exceptions.ApiRuntimeException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ListAppsService {
  private final AppRepository appRepository;

  @Value("${admin.api-key}")
  private String adminApiKey;
  
  private final String prefix = AppsErrorPrefix.MODULE + "." + AppsErrorPrefix.ROUTE_LIST_APPS + ".";

  public ListAppsService(AppRepository appRepository) {
    this.appRepository = appRepository;
  }

  public List<App> execute(String adminKey) {
    validateAdminKey(adminKey);
    return appRepository.findAll();
  }

  private void validateAdminKey(String adminKey) {
    if (!adminApiKey.equals(adminKey)) {
      throw new ApiRuntimeException(prefix + 1, "Access denied, invalid admin key.");
    }
  }
}
