package com.alisondev.live_storage_hub.controller;

import com.alisondev.live_storage_hub.entity.App;
import com.alisondev.live_storage_hub.repository.AppRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.List;

@RestController
@RequestMapping("/apps")
public class AppController {
  private final AppRepository appRepository;

  @Value("${admin.api-key}")
  private String adminApiKey;

  public AppController(AppRepository appRepository) {
    this.appRepository = appRepository;
  }

  @PostMapping
  public App createApp(@RequestHeader("X-ADMIN-KEY") String key,
      @RequestBody App app) {
    validateAdminKey(key);
    app.setApiKey(generateApiKey());
    return appRepository.save(app);
  }

  @GetMapping
  public List<App> listApps(@RequestHeader("X-ADMIN-KEY") String key) {
    validateAdminKey(key);
    return appRepository.findAll();
  }

  private void validateAdminKey(String key) {
    if (!adminApiKey.equals(key)) {
      throw new RuntimeException("Acesso negado: chave inválida");
    }
  }

  private String generateApiKey() {
    byte[] randomBytes = new byte[32];
    new SecureRandom().nextBytes(randomBytes);
    return Base64.getUrlEncoder().withoutPadding().encodeToString(randomBytes);
  }
}
