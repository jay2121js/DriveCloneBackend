package com.jay21213.googledrivebd.Entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "users")
public class User {

    @Id
    private String id;

    @Indexed(unique = true)
    private String email;

    @JsonIgnore
    private String password;

    private String name;

    private String profilePictureUrl;

    @Builder.Default
    private Long totalStorageLimit = 1073741824L; // 1GB default

    @Builder.Default
    private Long usedStorage = 0L;

    @Builder.Default
    private String role = "user";
}
