package com.vicary.zalandoscraper.configuration;

import com.vicary.zalandoscraper.api_telegram.service.RequestService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RequestServiceConfiguration {

    @Bean
    RequestService getRS() {
        return new RequestService();
    }
}
