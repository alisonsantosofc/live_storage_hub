package com.alisondev.live_storage_hub.controller;

import com.alisondev.live_storage_hub.security.JwtUtil;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AuthControllerTest {
  private static final String SECRET = "test-secret-with-at-least-32-characters";

  @Test
  void generatedTokenContainsUserAndAppIdentity() {
    JwtUtil jwtUtil = new JwtUtil(SECRET, 60_000);
    String token = jwtUtil.generateToken(10L, "user@example.com", 20L);

    assertTrue(jwtUtil.validateToken(token));
    assertEquals(10L, jwtUtil.getUserIdFromToken(token));
    assertEquals(20L, jwtUtil.getAppIdFromToken(token));
    assertEquals("user@example.com", jwtUtil.getEmailFromToken(token));
  }

  @Test
  void tokenSignedWithAnotherKeyIsRejected() {
    JwtUtil issuer = new JwtUtil(SECRET, 60_000);
    JwtUtil verifier = new JwtUtil("another-test-secret-with-32-characters", 60_000);
    String token = issuer.generateToken(10L, "user@example.com", 20L);

    assertFalse(verifier.validateToken(token));
  }
}
