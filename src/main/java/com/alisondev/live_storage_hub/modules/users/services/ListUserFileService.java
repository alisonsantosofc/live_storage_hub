package com.alisondev.live_storage_hub.modules.users.services;

import com.alisondev.live_storage_hub.modules.apps.entities.App;
import com.alisondev.live_storage_hub.modules.users.entities.User;
import com.alisondev.live_storage_hub.modules.users.entities.UserFile;
import com.alisondev.live_storage_hub.modules.apps.repositories.AppRepository;
import com.alisondev.live_storage_hub.modules.users.repositories.UserRepository;
import com.alisondev.live_storage_hub.modules.users.repositories.UserFileRepository;
import com.alisondev.live_storage_hub.modules.users.errors.UsersErrorPrefix;
import com.alisondev.live_storage_hub.exceptions.ApiRuntimeException;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ListUserFileService {
  private final AppRepository appRepository;
  private final UserRepository userRepository;
  private final UserFileRepository userFileRepository;
  private final String prefix = UsersErrorPrefix.MODULE + "." + UsersErrorPrefix.ROUTE_REGISTER_USER_DATA + ".";

  public ListUserFileService(AppRepository appRepository, UserRepository userRepository,
      UserFileRepository userFileRepository) {
    this.appRepository = appRepository;
    this.userRepository = userRepository;
    this.userFileRepository = userFileRepository;
  }

  public List<UserFile> execute(Long appId, Long userId) {
    App app = appRepository.findById(appId)
        .orElseThrow(() -> new ApiRuntimeException(prefix + 1, "App not found or invalid api key."));
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new ApiRuntimeException(prefix + 2, "User not found or invalid user id."));

    if (!user.getApp().getId().equals(appId)) {
      throw new ApiRuntimeException(prefix + 3, "User does not registered to this app.");
    }

    return userFileRepository.findByAppAndUser(app, user);
  }
}
