package com.jay21213.googledrivebd.Utils;

import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


@Slf4j
@Service
public class Cookies {

    @Value("${app.environment}")
    private String environment;

    private boolean isProduction() {
        return "production".equalsIgnoreCase(environment);
    }

    public void setCookies(String jwt, HttpServletResponse response) {
        int maxAge = 24 * 60 * 60;
        boolean secure = isProduction();
        // JWT cookie (HttpOnly)
        String jwtCookie = buildCookie("jwt", jwt, maxAge, secure, true);
        response.addHeader("Set-Cookie", jwtCookie);
        log.info("Set JWT cookie: {}", jwtCookie);
    }

    private String buildCookie(String name, String value, int maxAge, boolean secure, boolean httpOnly) {
        StringBuilder cookie = new StringBuilder();
        cookie.append(name).append("=").append(value)
                .append("; Path=/")
                .append("; Max-Age=").append(maxAge)
                .append("; SameSite=None");

        if (secure) {
            cookie.append("; Secure");
        }
        if (httpOnly) {
            cookie.append("; HttpOnly");
        }
        return cookie.toString();
    }
}