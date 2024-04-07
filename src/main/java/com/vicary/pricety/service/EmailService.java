package com.vicary.pricety.service;

import com.vicary.pricety.sender.EmailSenderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;

@Component
@RequiredArgsConstructor
public class EmailService {

    private final EmailSenderService emailSenderService;

    private final TemplateEngine templateEngine;

}
