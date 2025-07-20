package com.alisondev.live_storage_hub.modules.users.services;

import com.alisondev.live_storage_hub.modules.apps.entities.App;
import com.alisondev.live_storage_hub.modules.users.entities.User;
import com.alisondev.live_storage_hub.modules.users.entities.UserFile;
import com.alisondev.live_storage_hub.modules.users.repositories.UserFileRepository;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserFileService {
  private final UserFileRepository userFileRepository;

  public UserFileService(UserFileRepository userFileRepository) {
    this.userFileRepository = userFileRepository;
  }

  public List<UserFile> findByAppAndUser(App app, User user) {
    return userFileRepository.findByAppAndUser(app, user);
  }

  public Optional<UserFile> findByAppAndUserAndId(App app, User user, Long id) {
    return userFileRepository.findByAppAndUserAndId(app, user, id);
  }

  public long countByAppAndUser(App app, User user) {
    return userFileRepository.countByAppAndUser(app, user);
  }

  public UserFile save(UserFile userFile) {
    return userFileRepository.save(userFile);
  }

  public void deleteById(Long id) {
    userFileRepository.deleteById(id);
  }
}
