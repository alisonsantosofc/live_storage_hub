package com.alisondev.live_storage_hub.modules.users.services;

import com.alisondev.live_storage_hub.modules.apps.entities.App;
import com.alisondev.live_storage_hub.modules.users.entities.User;
import com.alisondev.live_storage_hub.modules.users.entities.VerificationCode;
import com.alisondev.live_storage_hub.modules.apps.repositories.AppRepository;
import com.alisondev.live_storage_hub.modules.users.repositories.UserRepository;
import com.alisondev.live_storage_hub.modules.users.repositories.VerificationCodeRepository;
import com.alisondev.live_storage_hub.modules.users.dtos.SendVerificationCodeDTO;
import com.alisondev.live_storage_hub.modules.users.errors.UsersErrorPrefix;
import com.alisondev.live_storage_hub.exceptions.ApiRuntimeException;

import org.springframework.stereotype.Service;

@Service
public class SendVerificationCodeService {
  private final AppRepository appRepository;
  private final UserRepository userRepository;
  private final VerificationCodeRepository verificationCodeRepository;
  private final String prefix = UsersErrorPrefix.MODULE + "." + UsersErrorPrefix.ROUTE_SEND_VERIFICATION_CODE + ".";

  public SendVerificationCodeService(AppRepository appRepository,
      UserRepository userRepository,
      VerificationCodeRepository verificationCodeRepository) {
    this.appRepository = appRepository;
    this.userRepository = userRepository;
    this.verificationCodeRepository = verificationCodeRepository;
  }

  public VerificationCode execute(Long appId, Long userId, SendVerificationCodeDTO request) {
    App app = appRepository.findById(appId)
        .orElseThrow(() -> new ApiRuntimeException(prefix + 1, "App not found or invalid api key."));
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new ApiRuntimeException(prefix + 2, "User not found or invalid user id."));

    if (!user.getApp().getId().equals(appId)) {
      throw new ApiRuntimeException(prefix + 3, "User does not registered to this app.");
    }

    // TODO - Gerar o código de verificação.

    // TODO - Gerar expires date baseado na data atual mais 14 horas. 

    VerificationCode verificationCode = VerificationCode.builder()
        .code("abc123")
        .app(app)
        .user(user)
        .build();

    return verificationCodeRepository.save(verificationCode);
  }
}
