package com.vicary.zalandoscraper.service;

import com.vicary.zalandoscraper.sender.EmailSenderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;

@Component
@RequiredArgsConstructor
public class EmailService {

    private final EmailSenderService emailSenderService;

    private final TemplateEngine templateEngine;

}
