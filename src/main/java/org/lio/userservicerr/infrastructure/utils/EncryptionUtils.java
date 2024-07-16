package org.lio.userservicerr.infrastructure.utils;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class EncryptionUtils {
    private final BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder(10);

    public String encrypt(String plainText) throws Exception {
        return bCryptPasswordEncoder.encode(plainText);
    }

    public boolean matches(String plainText, String encryptedText) {
        return bCryptPasswordEncoder.matches(plainText, encryptedText);
    }
}