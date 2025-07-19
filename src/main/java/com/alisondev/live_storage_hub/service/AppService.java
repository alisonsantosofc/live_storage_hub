package com.alisondev.live_storage_hub.service;

import com.alisondev.live_storage_hub.entity.App;
import com.alisondev.live_storage_hub.repository.AppRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AppService {
  private final AppRepository appRepository;

  public AppService(AppRepository appRepository) {
    this.appRepository = appRepository;
  }

  public List<App> findAll() {
    return appRepository.findAll();
  }

  public Optional<App> findById(Long id) {
    return appRepository.findById(id);
  }

  public Optional<App> findByApiKey(String apiKey) {
    return appRepository.findByApiKey(apiKey);
  }

  public App save(App app) {
    return appRepository.save(app);
  }

  public void deleteById(Long id) {
    appRepository.deleteById(id);
  }
}
