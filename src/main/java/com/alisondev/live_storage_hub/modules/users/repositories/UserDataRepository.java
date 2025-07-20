package com.alisondev.live_storage_hub.modules.users.repositories;

import com.alisondev.live_storage_hub.modules.apps.entities.App;
import com.alisondev.live_storage_hub.modules.users.entities.User;
import com.alisondev.live_storage_hub.modules.users.entities.UserData;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface UserDataRepository extends JpaRepository<UserData, Long> {
  List<UserData> findByAppAndUser(App app, User user);

  Optional<UserData> findByAppAndUserAndId(App app, User user, Long id);
}