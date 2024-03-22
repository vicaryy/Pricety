package com.vicary.zalandoscraper.configuration;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@ConfigurationProperties("secret-key-configuration")
@Configuration
@Getter
@Setter
class SecretKeyConfiguration {
    private String secretKey;
}
