package com.jay21213.googledrivebd.Services.Authentication;


import com.jay21213.googledrivebd.Entity.User;
import com.jay21213.googledrivebd.Services.UserService;
import com.jay21213.googledrivebd.Utils.JwtUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;

import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@Slf4j
public class AuthMangement {

    private UserService userService;
    private JwtUtil jwtUtil;

    @Autowired
    public AuthMangement(UserService userService, JwtUtil jwtUtil) {
        this.userService = userService;
        this.jwtUtil = jwtUtil;
    }

    public ResponseEntity<Map<String, Object>> getSession(HttpServletRequest request) {
        String jwt = Arrays.stream(Optional.ofNullable(request.getCookies()).orElse(new Cookie[0]))
                .filter(cookie -> "jwt".equals(cookie.getName()))
                .map(Cookie::getValue)
                .findFirst()
                .orElse(null);

        if (jwt == null) {
            return ResponseEntity.status(401).body(Map.of("error", "JWT cookie missing"));
        }

        try {
            // ✅ Validate token first
            if (!jwtUtil.validateToken(jwt)) {
                return ResponseEntity.status(401).body(Map.of("error", "Invalid or expired token"));
            }

            String email = jwtUtil.extractUsername(jwt);
            User user = userService.getUser(email);

            if (user == null) {
                return ResponseEntity.status(401).body(Map.of("error", "User not found"));
            }

            Map<String, Object> userData = new HashMap<>();
            userData.put("id", user.getId());
            userData.put("name", user.getName());
            userData.put("email", user.getEmail());
            userData.put("avatar", user.getProfilePictureUrl());
            userData.put("role", user.getRole());

            return ResponseEntity.ok(userData);

        } catch (Exception e) {
            // ✅ Log for debugging
            log.error("Error extracting session from JWT", e);
            return ResponseEntity.status(401).body(Map.of("error", "Unauthorized"));
        }
    }


    public ResponseEntity<Void> logout(HttpServletResponse response) {
        // ✅ Invalidate JWT cookie
        ResponseCookie jwtCookie = ResponseCookie.from("jwt", "")
                .httpOnly(true)
                .secure(false) // use true in production with HTTPS
                .path("/")
                .maxAge(0)
                .sameSite("Lax")
                .build();

        response.addHeader("Set-Cookie", jwtCookie.toString());
        log.error("Cookies s"+jwtCookie.toString());
        return ResponseEntity.ok().build();
    }

}