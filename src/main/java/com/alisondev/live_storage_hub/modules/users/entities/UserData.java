package com.alisondev.live_storage_hub.modules.users.entities;

import com.alisondev.live_storage_hub.modules.apps.entities.App;
import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import jakarta.persistence.*;
import lombok.*;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.Map;

@Entity
@Table(name = "users_data")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserData {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "app_id", nullable = false)
  private App app;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id", nullable = false)
  private User user;

  @Type(JsonBinaryType.class)
  @Column(name = "json_data", columnDefinition = "jsonb", nullable = false)
  private Map<String, Object> jsonData;

  @CreationTimestamp
  @Column(name = "created_at", updatable = false)
  private LocalDateTime createdAt;

  @UpdateTimestamp
  @Column(name = "updated_at")
  private LocalDateTime updatedAt;
}
