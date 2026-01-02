package com.jay21213.googledrivebd.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserAuthReponse {
    private String username;
    private String avatar;
    private String error;

    public UserAuthReponse(String email, String profilePictureUrl) {
        this.username = email;
        this.avatar = profilePictureUrl;

    }

}