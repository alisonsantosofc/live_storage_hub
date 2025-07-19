package com.alisondev.live_storage_hub.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

@Configuration
public class StorageConfig {
  @Value("${storage.mode:local}")
  private String storageMode;

  @Value("${storage.local-path:./tmp/uploads}")
  private String localPath;

  @Value("${aws.s3.bucket:}")
  private String bucketName;

  @Value("${aws.s3.region:us-east-1}")
  private String region;

  @Value("${aws.credentials.access-key:}")
  private String awsAccessKey;

  @Value("${aws.credentials.secret-key:}")
  private String awsSecretKey;

  public String getStorageMode() {
    return storageMode;
  }

  public String getLocalPath() {
    return localPath;
  }

  public String getBucketName() {
    return bucketName;
  }

  @Bean
  @ConditionalOnProperty(name = "storage.mode", havingValue = "s3")
  public S3Client s3Client() {
    return S3Client.builder()
        .region(Region.of(region))
        .credentialsProvider(StaticCredentialsProvider.create(
            AwsBasicCredentials.create(awsAccessKey, awsSecretKey)))
        .build();
  }
}
