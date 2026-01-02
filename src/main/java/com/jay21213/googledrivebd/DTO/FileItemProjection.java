package com.jay21213.googledrivebd.DTO;


import java.time.Instant;

public interface FileItemProjection {
    String getId();
    String getName();
    Long getSize();
    String getCloudinaryUrl();
    Boolean getFavorite();
    String getMimeType();
    Instant getCreatedAt();
    Instant getUpdatedAt();

}
