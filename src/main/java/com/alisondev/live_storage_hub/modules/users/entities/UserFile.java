package com.alisondev.live_storage_hub.modules.users.entities;

import com.alisondev.live_storage_hub.modules.apps.entities.App;
import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import jakarta.persistence.*;
import lombok.*;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Type;

import java.time.LocalDateTime;
import java.util.Map;

@Entity
@Table(name = "users_file")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserFile {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "app_id", nullable = false)
  private App app;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id", nullable = false)
  private User user;

  @Column(name = "file_url", nullable = false)
  private String fileUrl;

  @Column(name = "file_type", length = 50)
  private String fileType; // image, backup, pdf

  @Type(JsonBinaryType.class)
  @Column(columnDefinition = "jsonb")
  private Map<String, Object> metadata;

  @CreationTimestamp
  @Column(name = "created_at", updatable = false)
  private LocalDateTime createdAt;
}
