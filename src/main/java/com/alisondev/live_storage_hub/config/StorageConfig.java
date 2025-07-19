package com.alisondev.live_storage_hub.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class StorageConfig {
  @Value("${storage.mode}")
  private String storageMode;

  @Value("${storage.local-path:/tmp/uploads}")
  private String localPath;

  @Value("${aws.s3.bucket:}")
  private String bucketName;

  @Value("${aws.s3.region:us-east-1}")
  private String awsRegion;

  public String getStorageMode() {
    return storageMode;
  }

  public String getLocalPath() {
    return localPath;
  }

  public String getBucketName() {
    return bucketName;
  }

  public String getAwsRegion() {
    return awsRegion;
  }
}
