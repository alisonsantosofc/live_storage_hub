package com.alisondev.live_storage_hub.modules.users.services;

import com.alisondev.live_storage_hub.modules.apps.entities.App;
import com.alisondev.live_storage_hub.modules.users.entities.User;
import com.alisondev.live_storage_hub.modules.users.entities.UserData;
import com.alisondev.live_storage_hub.modules.apps.repositories.AppRepository;
import com.alisondev.live_storage_hub.modules.users.repositories.UserRepository;
import com.alisondev.live_storage_hub.modules.users.repositories.UserDataRepository;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ListUserDataService {
  private final AppRepository appRepository;
  private final UserRepository userRepository;
  private final UserDataRepository userDataRepository;

  public ListUserDataService(AppRepository appRepository, UserRepository userRepository,
      UserDataRepository userDataRepository) {
    this.appRepository = appRepository;
    this.userRepository = userRepository;
    this.userDataRepository = userDataRepository;
  }

  public List<UserData> execute(Long appId, Long userId) {
    App app = appRepository.findById(appId)
        .orElseThrow(() -> new RuntimeException("App não encontrado"));
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

    if (!user.getApp().getId().equals(appId)) {
      throw new RuntimeException("Usuário não pertence a este App");
    }

    return userDataRepository.findByAppAndUser(app, user);
  }
}
