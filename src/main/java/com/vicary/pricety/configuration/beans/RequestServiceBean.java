package com.vicary.pricety.configuration.beans;

import com.vicary.pricety.api_telegram.service.RequestService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RequestServiceBean {

    @Bean
    RequestService getRS() {
        return new RequestService();
    }
}
