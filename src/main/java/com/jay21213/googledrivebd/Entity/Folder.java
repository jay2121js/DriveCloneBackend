package com.jay21213.googledrivebd.Entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
@Document(collection = "folders")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Folder {

    @Id
    private String id;

    @Indexed
    private String userId;

    @Indexed
    private String parentFolderId;

    private String name;

    @Builder.Default
    private Boolean favorite = false;

    @Builder.Default
    private int items = 0;

    @CreatedDate
    private Instant createdAt;

    @LastModifiedDate
    private Instant updatedAt;

    private List<String> ancestors;

    @Builder.Default
    private Boolean trashed = false; // send to trash, soft delete
}
