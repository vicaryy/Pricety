package com.vicary.zalandoscraper.configuration.beans;

import com.vicary.zalandoscraper.api_telegram.service.QuickSender;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class QuickSenderBean {

    @Bean
    public QuickSender getQuickSender() {
        return new QuickSender();
    }
}
