package com.alisondev.live_storage_hub.service;

import com.alisondev.live_storage_hub.entity.App;
import com.alisondev.live_storage_hub.entity.User;
import com.alisondev.live_storage_hub.entity.UserData;
import com.alisondev.live_storage_hub.repository.UserDataRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class UserDataService {
  private final UserDataRepository userDataRepository;

  public UserDataService(UserDataRepository userDataRepository) {
    this.userDataRepository = userDataRepository;
  }

  public List<UserData> findByAppAndUser(App app, User user) {
    return userDataRepository.findByAppAndUser(app, user);
  }

  public Optional<UserData> findByAppAndUserAndId(App app, User user, Long id) {
    return userDataRepository.findByAppAndUserAndId(app, user, id);
  }

  public UserData save(UserData userData) {
    userData.setUpdatedAt(LocalDateTime.now());
    if (userData.getCreatedAt() == null) {
      userData.setCreatedAt(LocalDateTime.now());
    }
    return userDataRepository.save(userData);
  }

  public void deleteById(Long id) {
    userDataRepository.deleteById(id);
  }
}
