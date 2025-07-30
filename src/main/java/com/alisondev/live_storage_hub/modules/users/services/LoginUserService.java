package com.alisondev.live_storage_hub.modules.users.services;


import com.alisondev.live_storage_hub.modules.apps.entities.App;
import com.alisondev.live_storage_hub.modules.users.entities.User;
import com.alisondev.live_storage_hub.modules.apps.repositories.AppRepository;
import com.alisondev.live_storage_hub.modules.users.repositories.UserRepository;
import com.alisondev.live_storage_hub.modules.users.dtos.AuthResponseDTO;
import com.alisondev.live_storage_hub.modules.users.dtos.LoginUserDTO;
import com.alisondev.live_storage_hub.modules.users.dtos.UserResponseDTO;
import com.alisondev.live_storage_hub.modules.users.errors.UsersErrorPrefix;
import com.alisondev.live_storage_hub.exceptions.ApiRuntimeException;
import com.alisondev.live_storage_hub.security.JwtUtil;


import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class LoginUserService {
  private final AppRepository appRepository;
  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final JwtUtil jwtUtil;
  private final String prefix = UsersErrorPrefix.MODULE + "." + UsersErrorPrefix.ROUTE_LOGIN_USER + ".";

  public LoginUserService(AppRepository appRepository,
      UserRepository userRepository,
      PasswordEncoder passwordEncoder,
      JwtUtil jwtUtil) {
    this.appRepository = appRepository;
    this.userRepository = userRepository;
    this.passwordEncoder = passwordEncoder;
    this.jwtUtil = jwtUtil;
  }

  public AuthResponseDTO execute(String apiKey, LoginUserDTO request) {
    App app = appRepository.findByApiKey(apiKey)
        .orElseThrow(() -> new ApiRuntimeException(prefix + 1, "App not found or invalid api key."));

    User user = userRepository.findByAppAndEmail(app, request.getEmail())
        .orElseThrow(() -> new ApiRuntimeException(prefix + 2, "User not found or invalid user id."));

    if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
      throw new ApiRuntimeException(prefix + 3, "Invalid email or password.");
    }

    String token = jwtUtil.generateToken(user.getEmail(), app.getId());

    AuthResponseDTO response = new AuthResponseDTO();
    response.setToken(token);

    UserResponseDTO userResponse = new UserResponseDTO();
    userResponse.setId(user.getId());
    userResponse.setName(user.getName());
    userResponse.setEmail(user.getEmail());
    userResponse.setCreatedAt(user.getCreatedAt());
    response.setUser(userResponse);

    return response;
  }
}
