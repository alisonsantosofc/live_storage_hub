package com.alisondev.live_storage_hub.repository;

import com.alisondev.live_storage_hub.entity.UserData;
import com.alisondev.live_storage_hub.entity.App;
import com.alisondev.live_storage_hub.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface UserDataRepository extends JpaRepository<UserData, Long> {
  List<UserData> findByAppAndUser(App app, User user);

  Optional<UserData> findByAppAndUserAndId(App app, User user, Long id);
}