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
import java.util.List;

@Document(collection = "files")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FileItem {

    @Id
    private String id;

    @Indexed
    private String userId;

    @Indexed
    private String folderId;

    @Indexed
    private String name;

    private String mimeType;

    private Long size;

    private String cloudinaryPublicId;

    private String cloudinaryUrl;

    private String cloudinaryType;

    private List<String> ancestors;

    @Builder.Default
    private Boolean favorite = false;

    @CreatedDate
    private Instant createdAt;

    @LastModifiedDate
    private Instant updatedAt;

    @Builder.Default
    private Boolean trashed = false;

    private Instant trashedAt;
}
