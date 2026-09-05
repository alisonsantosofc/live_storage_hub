package com.alisondev.live_storage_hub.controller;

import com.alisondev.live_storage_hub.exceptions.ApiRuntimeException;
import com.alisondev.live_storage_hub.modules.apps.entities.App;
import com.alisondev.live_storage_hub.modules.apps.repositories.AppRepository;
import com.alisondev.live_storage_hub.modules.users.entities.User;
import com.alisondev.live_storage_hub.modules.users.entities.UserData;
import com.alisondev.live_storage_hub.modules.users.repositories.UserDataRepository;
import com.alisondev.live_storage_hub.modules.users.repositories.UserRepository;
import com.alisondev.live_storage_hub.modules.users.services.ListUserDataService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserDataControllerTest {
  @Mock private AppRepository appRepository;
  @Mock private UserRepository userRepository;
  @Mock private UserDataRepository userDataRepository;

  private ListUserDataService service;

  @BeforeEach
  void setUp() {
    service = new ListUserDataService(appRepository, userRepository, userDataRepository);
  }

  @Test
  void listsOnlyDataOwnedByAuthenticatedAppAndUser() {
    App app = App.builder().id(1L).build();
    User user = User.builder().id(2L).app(app).build();
    List<UserData> expected = List.of(UserData.builder().id(3L).app(app).user(user).build());
    when(appRepository.findById(1L)).thenReturn(Optional.of(app));
    when(userRepository.findById(2L)).thenReturn(Optional.of(user));
    when(userDataRepository.findByAppAndUser(app, user)).thenReturn(expected);

    assertEquals(expected, service.execute(1L, 2L));
    verify(userDataRepository).findByAppAndUser(app, user);
  }

  @Test
  void rejectsUserFromAnotherApp() {
    App requestedApp = App.builder().id(1L).build();
    App userApp = App.builder().id(9L).build();
    User user = User.builder().id(2L).app(userApp).build();
    when(appRepository.findById(1L)).thenReturn(Optional.of(requestedApp));
    when(userRepository.findById(2L)).thenReturn(Optional.of(user));

    ApiRuntimeException error = assertThrows(ApiRuntimeException.class,
        () -> service.execute(1L, 2L));

    assertEquals("1.5.3", error.getCode());
    verifyNoInteractions(userDataRepository);
  }
}
