package com.alisondev.live_storage_hub.repository;

import com.alisondev.live_storage_hub.entity.App;
import com.alisondev.live_storage_hub.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
  Optional<User> findByAppAndEmail(App app, String email);

  boolean existsByAppAndEmail(App app, String email);
}
