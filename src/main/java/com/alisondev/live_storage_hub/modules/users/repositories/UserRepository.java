package com.alisondev.live_storage_hub.modules.users.repositories;

import com.alisondev.live_storage_hub.modules.apps.entities.App;
import com.alisondev.live_storage_hub.modules.users.entities.User;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
  Optional<User> findByAppAndEmail(App app, String email);

  boolean existsByAppAndEmail(App app, String email);
}
