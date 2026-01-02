package com.jay21213.googledrivebd.DTO;


import java.time.Instant;

public interface FolderProjection {
    String getId();
    String getName();
    Integer getItems();
    Boolean getFavorite(); // matches entity field
    Instant getCreatedAt();
    Instant getUpdatedAt();
}

