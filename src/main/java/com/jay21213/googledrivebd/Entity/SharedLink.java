package com.jay21213.googledrivebd.Entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "shared_links")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SharedLink {

    @Id
    private String id;

    @Indexed
    private String resourceId; // fileId or folderId

    private String resourceType; // FILE or FOLDER

    @Indexed(unique = true)
    private String shareToken; // used in public share URLs

    @Builder.Default
    private boolean readOnly = true;

    private Long expiresAt; // null = never expires

    private String ownerId;
}
