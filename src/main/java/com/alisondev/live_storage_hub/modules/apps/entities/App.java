package com.alisondev.live_storage_hub.modules.apps.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;

import com.alisondev.live_storage_hub.modules.users.entities.User;

@Entity
@Table(name = "apps")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class App {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private String name;

  @Column(name = "api_key", nullable = false, unique = true)
  private String apiKey;

  @CreationTimestamp
  @Column(name = "created_at", updatable = false)
  private LocalDateTime createdAt;

  @OneToMany(mappedBy = "app", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  private List<User> users;
}
