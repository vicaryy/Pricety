package com.vicary.pricety.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SecretKey {

    private static SecretKeyConfiguration secretKeyConfiguration;

    @Autowired
    public SecretKey(SecretKeyConfiguration secretKeyConfiguration) {
        SecretKey.secretKeyConfiguration = secretKeyConfiguration;
    }

    public static String get() {
        return secretKeyConfiguration.getSecretKey();
    }
}
