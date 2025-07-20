package com.alisondev.live_storage_hub.modules.apps.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.alisondev.live_storage_hub.modules.apps.entities.App;

import java.util.Optional;

public interface AppRepository extends JpaRepository<App, Long> {
  Optional<App> findByApiKey(String apiKey);

  Optional<App> findByName(String name);
}
