package com.vicary.pricety.configuration.beans;

import com.vicary.pricety.utils.ApplicationCrasher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationCrasherBean {

    @Bean
    public ApplicationCrasher getAC() {
        return new ApplicationCrasher();
    }
}
