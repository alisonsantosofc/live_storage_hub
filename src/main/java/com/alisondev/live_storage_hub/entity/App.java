package com.alisondev.live_storage_hub.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

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

  @Column(name = "created_at", nullable = false, updatable = false)
  private LocalDateTime createdAt = LocalDateTime.now();

  @OneToMany(mappedBy = "app", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  private List<User> users;
}
