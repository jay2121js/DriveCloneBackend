package com.jay21213.googledrivebd.Controller;


import com.jay21213.googledrivebd.DTO.UserAuthReponse;
import com.jay21213.googledrivebd.Services.Authentication.AuthHandler;
import com.jay21213.googledrivebd.Services.Authentication.AuthMangement;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RequestMapping("/google")
@RestController
public class GoogleAuthController {

    private AuthHandler authHandler;
    private AuthMangement authMangement;

    @Autowired
    public GoogleAuthController(AuthMangement authMangement, AuthHandler authHandler) {
        this.authMangement = authMangement;
        this.authHandler = authHandler;
    }


    @GetMapping
    public String Check() {
        return "Redirect to Google OAuth2 login page";
    }

    @Transactional
    @PostMapping("/signin")
    public ResponseEntity<UserAuthReponse> googleLogin(@RequestParam String code, HttpServletResponse response) {
        return authHandler.googleSignin(code, response);
    }

    @GetMapping("/session")
    public ResponseEntity<Map<String, Object>> getSession(HttpServletRequest request) {
        return authMangement.getSession(request);
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletResponse response) {
        return  authMangement.logout(response);
    }


}
