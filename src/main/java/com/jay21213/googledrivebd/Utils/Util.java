package com.jay21213.googledrivebd.Utils;

import org.springframework.stereotype.Service;

@Service
public class Util {
    public String generateOtp() {
        return String.valueOf((int)(Math.random() * 900000) + 100000); // 6-digit
    }


}
