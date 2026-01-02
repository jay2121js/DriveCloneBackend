package com.jay21213.googledrivebd.Services.Authentication;


import com.jay21213.googledrivebd.DTO.UserAuthReponse;
import com.jay21213.googledrivebd.Entity.User;
import com.jay21213.googledrivebd.Services.FolderService;
import com.jay21213.googledrivebd.Utils.Cookies;
import com.jay21213.googledrivebd.Utils.JwtUtil;
import com.jay21213.googledrivebd.Services.UserService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Slf4j
@Service
public class AuthHandler {
        @Value("${spring.security.oauth2.client.registration.google.client-id}")
        private String clientId;
        @Value("${spring.security.oauth2.client.registration.google.client-secret}")
        String clientSecret;

    @Value("${Frontend_URI}")
    private String frontendUrl;


    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private Cookies cookies;
    @Autowired
    private JwtUtil  jwtUtil;
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private FolderService folderService;


        public Map  getGoogleUserInfo(String code, String redirectUri) {
        String tokenUrl = "https://oauth2.googleapis.com/token";
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("code", code);
        params.add("client_id", clientId);
        params.add("client_secret", clientSecret);
        params.add("redirect_uri", redirectUri);
        params.add("grant_type", "authorization_code");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity<MultiValueMap<String, String>> tokenRequest = new HttpEntity<>(params, headers);

        try {
            ResponseEntity<Map> tokenResponse = restTemplate.postForEntity(tokenUrl, tokenRequest, Map.class);
            if (!tokenResponse.getStatusCode().is2xxSuccessful() || tokenResponse.getBody() == null) {
                log.error("Failed to get access token: {}", tokenResponse.getStatusCode());
                return null;
            }

            String idToken = (String) tokenResponse.getBody().get("id_token");
            if (idToken == null) {
                log.error("ID token is missing in the response.");
                return null;
            }

            String userInfoUrl = "https://oauth2.googleapis.com/tokeninfo?id_token=" + idToken;
            ResponseEntity<Map> userInfoResponse = restTemplate.getForEntity(userInfoUrl, Map.class);

            if (!userInfoResponse.getStatusCode().is2xxSuccessful() || userInfoResponse.getBody() == null) {
                log.error("Failed to fetch user info: {}", userInfoResponse.getStatusCode());
                return null;
            }

            return userInfoResponse.getBody();
        } catch (Exception e) {
            log.error("Exception occurred while exchanging code: {}", e.getMessage(), e);
            return null;
        }
    }


    @Transactional
    public ResponseEntity<UserAuthReponse> googleSignin(String code, HttpServletResponse response){
        try{
            Map<String, Object> userInfo = getGoogleUserInfo (code, frontendUrl);

            if (userInfo == null || !userInfo.containsKey("email")) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(new UserAuthReponse(null, null, "Failed to fetch user info from Google"));
            }
            String email = userInfo.get("email").toString();
            User existingUser = userService.getUser(email);
            if (existingUser != null) {
                String jwt = jwtUtil.generateToken(email);
                cookies.setCookies(jwt, response);
                return ResponseEntity.ok(new UserAuthReponse(existingUser.getEmail(), existingUser.getProfilePictureUrl()));
            }
            String name = userInfo.get("name").toString();
            String picture =  userInfo.get("picture").toString();
            User user = new User();
            user.setEmail(email);
            user.setName(name);
            user.setPassword(passwordEncoder.encode("google_oauth_" + email));
            user.setProfilePictureUrl(picture);
            user = userService.saveUser(user);
            folderService.createFolder("root", user.getId(), null, user.getId());
            String jwt = jwtUtil.generateToken(email);
            cookies.setCookies(jwt, response);
            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(new UserAuthReponse(user.getEmail(), user.getProfilePictureUrl()));

        } catch (Exception e) {
            // Log the error and rethrow to ensure transaction rollback
            log.error("Signup failed: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new UserAuthReponse(null, null, "An internal error occurred during Google sign-in."));
        }
    }
}
