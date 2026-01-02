package com.jay21213.googledrivebd.DTO;

import lombok.Data;

import java.util.List;

@Data
public class DriveListResponse {
    List<FolderProjection> folders;
    private List<FileItemProjection> fileItems;
    private List<AncestorProjection> ancestor;
    private String id;

    // getters / setters
}
