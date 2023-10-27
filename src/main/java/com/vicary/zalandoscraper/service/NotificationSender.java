package com.vicary.zalandoscraper.service;

import com.vicary.zalandoscraper.entity.NotificationEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificationSender{

    public void send(List<NotificationEntity> notifications) {
        System.out.println("SENDING...");
        //todo
    }

}
