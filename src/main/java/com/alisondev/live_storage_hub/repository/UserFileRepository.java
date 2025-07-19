package com.alisondev.live_storage_hub.repository;

import com.alisondev.live_storage_hub.entity.UserFile;
import com.alisondev.live_storage_hub.entity.App;
import com.alisondev.live_storage_hub.entity.User;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserFileRepository extends JpaRepository<UserFile, Long> {
  List<UserFile> findByAppAndUser(App app, User user);

  Optional<UserFile> findByAppAndUserAndId(App app, User user, Long id);

  long countByAppAndUser(App app, User user);
}
