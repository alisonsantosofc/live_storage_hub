package com.alisondev.live_storage_hub.service;

import com.alisondev.live_storage_hub.entity.App;
import com.alisondev.live_storage_hub.entity.User;
import com.alisondev.live_storage_hub.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
  private final UserRepository userRepository;

  public UserService(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  public List<User> findByApp(App app) {
    return userRepository.findAll().stream()
        .filter(user -> user.getApp().equals(app))
        .toList();
  }

  public Optional<User> findById(Long id) {
    return userRepository.findById(id);
  }

  public Optional<User> findByAppAndEmail(App app, String email) {
    return userRepository.findByAppAndEmail(app, email);
  }

  public boolean existsByAppAndEmail(App app, String email) {
    return userRepository.existsByAppAndEmail(app, email);
  }

  public User save(User user) {
    return userRepository.save(user);
  }

  public void deleteById(Long id) {
    userRepository.deleteById(id);
  }
}
