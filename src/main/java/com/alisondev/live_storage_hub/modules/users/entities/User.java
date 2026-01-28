package com.alisondev.live_storage_hub.modules.users.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;

import com.alisondev.live_storage_hub.modules.apps.entities.App;

@Entity
@Table(name = "users", uniqueConstraints = {
    @UniqueConstraint(columnNames = { "app_id" })
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private String name;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "app_id", nullable = false)
  private App app;

  @Column(nullable = false)
  private String email;

  @Column(nullable = false)
  private String password;

  @CreationTimestamp
  @Column(name = "created_at", updatable = false)
  private LocalDateTime createdAt;

  @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<VerificationCode> verificationCodes = new ArrayList<>();
}
