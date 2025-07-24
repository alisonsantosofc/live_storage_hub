package com.alisondev.live_storage_hub.modules.users.services;

import com.alisondev.live_storage_hub.modules.apps.entities.App;
import com.alisondev.live_storage_hub.modules.users.entities.User;
import com.alisondev.live_storage_hub.modules.users.entities.UserFile;
import com.alisondev.live_storage_hub.modules.apps.repositories.AppRepository;
import com.alisondev.live_storage_hub.modules.users.repositories.UserRepository;
import com.alisondev.live_storage_hub.modules.users.repositories.UserFileRepository;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ListUserFileService {
  private final AppRepository appRepository;
  private final UserRepository userRepository;
  private final UserFileRepository userFileRepository;

  public ListUserFileService(AppRepository appRepository, UserRepository userRepository,
      UserFileRepository userFileRepository) {
    this.appRepository = appRepository;
    this.userRepository = userRepository;
    this.userFileRepository = userFileRepository;
  }

  public List<UserFile> execute(Long appId, Long userId) {
    App app = appRepository.findById(appId)
        .orElseThrow(() -> new RuntimeException("App não encontrado"));
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

    if (!user.getApp().getId().equals(appId)) {
      throw new RuntimeException("Usuário não pertence a este App");
    }

    return userFileRepository.findByAppAndUser(app, user);
  }
}
