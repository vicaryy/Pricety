package com.vicary.zalandoscraper.updater.sender;

import com.vicary.zalandoscraper.exception.ScraperBotException;
import com.vicary.zalandoscraper.model.Email;
import com.vicary.zalandoscraper.sender.EmailSender;
import com.vicary.zalandoscraper.service.UpdateReceiverService;
import com.vicary.zalandoscraper.service.repository_services.NotificationEmailService;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
class EmailNotificationSenderTest {

    @Autowired
    private EmailNotificationSender sender;

    @MockBean
    private EmailSender emailSender;

    @MockBean
    private NotificationEmailService notificationEmailService;

    @MockBean
    private UpdateReceiverService updateReceiverService;


    @SneakyThrows
    @Test
    void send_NormalTenEmails() {
        //given
        List<Email> givenTenNotifications = getValidListOfTenNotifications();

        //when
        sender.send(givenTenNotifications);

        //then
        assertEquals(10, sender.getSentAmountAndReset());
        assertEquals(0, sender.getFailedAmount());
        verify(emailSender, times(10)).send(any(Email.class));
        verify(notificationEmailService, times(0)).saveEntity(any(Email.class));
    }

    @SneakyThrows
    @Test
    void send_NormalTenEmailsAndSaveToRepository() {
        //given
        List<Email> givenTenNotifications = getValidListOfTenNotifications();

        //when
        sender.sendAndSave(givenTenNotifications);

        //then
        assertEquals(10, sender.getSentAmountAndReset());
        assertEquals(0, sender.getFailedAmount());
        verify(emailSender, times(10)).send(any(Email.class));
        verify(notificationEmailService, times(10)).saveEntity(any(Email.class));
    }

    @SneakyThrows
    @Test
    void send_NormalElevenEmailsButHeFailedOne() {
        //given
        Email givenFailedNotification = new Email("to", "title", "fail", false);
        List<Email> givenElevenNotifications = getValidListOfTenNotifications();
        givenElevenNotifications.add(givenFailedNotification);

        //when
        doThrow(ScraperBotException.class).when(emailSender).send(givenFailedNotification);
        sender.send(givenElevenNotifications);

        //then
        assertEquals(10, sender.getSentAmountAndReset());
        assertEquals(1, sender.getFailedAmount());
        verify(emailSender, times(11)).send(any(Email.class));
        verify(notificationEmailService, times(0)).saveEntity(any(Email.class));
    }




    private List<Email> getValidListOfTenNotifications() {
        return new ArrayList<>(Arrays.asList(
                new Email("123", "text", "text", false),
                new Email("123", "text", "text", false),
                new Email("123", "text", "text", false),
                new Email("123", "text", "text", false),
                new Email("123", "text", "text", false),
                new Email("123", "text", "text", false),
                new Email("123", "text", "text", false),
                new Email("123", "text", "text", false),
                new Email("123", "text", "text", false),
                new Email("123", "text", "text", false)
        ));
    }

}