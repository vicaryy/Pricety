package com.vicary.zalandoscraper.configuration.beans;

import com.vicary.zalandoscraper.utils.ApplicationCrasher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationCrasherBean {

    @Bean
    public ApplicationCrasher getAC() {
        return new ApplicationCrasher();
    }
}
