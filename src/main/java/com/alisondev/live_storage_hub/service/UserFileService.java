package com.alisondev.live_storage_hub.service;

import com.alisondev.live_storage_hub.entity.App;
import com.alisondev.live_storage_hub.entity.User;
import com.alisondev.live_storage_hub.entity.UserFile;
import com.alisondev.live_storage_hub.repository.UserFileRepository;
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
