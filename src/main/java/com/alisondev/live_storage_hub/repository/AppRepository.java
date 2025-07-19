package com.alisondev.live_storage_hub.repository;

import com.alisondev.live_storage_hub.entity.App;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface AppRepository extends JpaRepository<App, Long> {
  Optional<App> findByApiKey(String apiKey);

  Optional<App> findByName(String name);
}
