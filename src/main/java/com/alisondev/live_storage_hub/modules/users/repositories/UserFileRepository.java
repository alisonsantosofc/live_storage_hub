package com.alisondev.live_storage_hub.modules.users.repositories;


import com.alisondev.live_storage_hub.modules.apps.entities.App;
import com.alisondev.live_storage_hub.modules.users.entities.User;
import com.alisondev.live_storage_hub.modules.users.entities.UserFile;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserFileRepository extends JpaRepository<UserFile, Long> {
  List<UserFile> findByAppAndUser(App app, User user);

  Optional<UserFile> findByAppAndUserAndId(App app, User user, Long id);

  long countByAppAndUser(App app, User user);
}
