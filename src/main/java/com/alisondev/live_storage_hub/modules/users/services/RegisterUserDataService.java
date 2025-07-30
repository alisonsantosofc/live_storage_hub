package com.alisondev.live_storage_hub.modules.users.services;


import com.alisondev.live_storage_hub.modules.apps.entities.App;
import com.alisondev.live_storage_hub.modules.users.entities.User;
import com.alisondev.live_storage_hub.modules.users.entities.UserData;
import com.alisondev.live_storage_hub.modules.apps.repositories.AppRepository;
import com.alisondev.live_storage_hub.modules.users.repositories.UserRepository;
import com.alisondev.live_storage_hub.modules.users.repositories.UserDataRepository;
import com.alisondev.live_storage_hub.modules.users.dtos.RegisterUserDataDTO;
import com.alisondev.live_storage_hub.modules.users.errors.UsersErrorPrefix;
import com.alisondev.live_storage_hub.exceptions.ApiRuntimeException;

import org.springframework.stereotype.Service;

@Service
public class RegisterUserDataService {
  private final AppRepository appRepository;
  private final UserRepository userRepository;
  private final UserDataRepository userDataRepository;
  private final String prefix = UsersErrorPrefix.MODULE + "." + UsersErrorPrefix.ROUTE_REGISTER_USER_DATA + ".";

  public RegisterUserDataService(AppRepository appRepository, UserRepository userRepository,
      UserDataRepository userDataRepository) {
    this.appRepository = appRepository;
    this.userRepository = userRepository;
    this.userDataRepository = userDataRepository;
  }

  public UserData execute(Long appId, Long userId, RegisterUserDataDTO request) {
    App app = appRepository.findById(appId)
        .orElseThrow(() -> new ApiRuntimeException(prefix + 1, "App not found or invalid api key."));
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new ApiRuntimeException(prefix + 2, "User not found or invalid user id."));

    if (!user.getApp().getId().equals(appId)) {
      throw new ApiRuntimeException(prefix + 3, "User does not registered to this app.");
    }

    UserData userData = UserData.builder()
        .app(app)
        .user(user)
        .jsonData(request.getJsonData())
        .build();

    return userDataRepository.save(userData);
  }
}
