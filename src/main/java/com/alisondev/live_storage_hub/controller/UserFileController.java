package com.alisondev.live_storage_hub.controller;

import com.alisondev.live_storage_hub.entity.*;
import com.alisondev.live_storage_hub.repository.*;
import com.alisondev.live_storage_hub.security.JwtUtil;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/user_file")
public class UserFileController {
  private final AppRepository appRepository;
  private final UserRepository userRepository;
  private final UserFileRepository userFileRepository;
  private final JwtUtil jwtUtil;
  private final S3Client s3Client;
  private final String bucketName = "seu-bucket";

  public UserFileController(AppRepository appRepository, UserRepository userRepository,
      UserFileRepository userFileRepository, JwtUtil jwtUtil) {
    this.appRepository = appRepository;
    this.userRepository = userRepository;
    this.userFileRepository = userFileRepository;
    this.jwtUtil = jwtUtil;

    this.s3Client = S3Client.builder()
        .region(Region.US_EAST_1)
        .credentialsProvider(ProfileCredentialsProvider.create())
        .build();
  }

  @PostMapping("/upload")
  public UserFile uploadFile(@RequestHeader("Authorization") String authHeader,
      @RequestParam("userId") Long userId,
      @RequestParam("file") MultipartFile file,
      @RequestParam("fileType") String fileType) throws IOException {
    String token = authHeader.substring(7);
    Long appId = jwtUtil.getAppIdFromToken(token);

    App app = appRepository.findById(appId).orElseThrow();
    User user = userRepository.findById(userId).orElseThrow();

    if (!user.getApp().getId().equals(appId)) {
      throw new RuntimeException("User does not registered in current application.");
    }

    String key = UUID.randomUUID() + "_" + file.getOriginalFilename();

    // Upload para S3
    s3Client.putObject(
        PutObjectRequest.builder().bucket(bucketName).key(key).build(),
        software.amazon.awssdk.core.sync.RequestBody.fromBytes(file.getBytes()));

    String fileUrl = "https://" + bucketName + ".s3.amazonaws.com/" + key;

    UserFile userFile = UserFile.builder()
        .app(app)
        .user(user)
        .fileType(fileType)
        .fileUrl(fileUrl)
        .metadata(Map.of("size", file.getSize(), "name", file.getOriginalFilename()))
        .build();

    return userFileRepository.save(userFile);
  }

  @GetMapping("/list")
  public List<UserFile> listFiles(@RequestHeader("Authorization") String authHeader,
      @RequestParam Long userId) {
    String token = authHeader.substring(7);
    Long appId = jwtUtil.getAppIdFromToken(token);

    App app = appRepository.findById(appId).orElseThrow();
    User user = userRepository.findById(userId).orElseThrow();

    if (!user.getApp().getId().equals(appId)) {
      throw new RuntimeException("User does not registered in current application.");
    }

    return userFileRepository.findByAppAndUser(app, user);
  }
}
