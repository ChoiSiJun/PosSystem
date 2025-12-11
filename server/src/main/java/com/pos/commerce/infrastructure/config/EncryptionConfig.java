package com.pos.commerce.infrastructure.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EncryptionConfig {

    @Value("${encryption.secret-key:MySecretKey123456789012345678901234567890}")
    private String secretKey;

    public String getSecretKey() {
        return secretKey;
    }
}

