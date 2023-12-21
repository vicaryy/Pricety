package com.vicary.zalandoscraper.updater.sender;

import com.vicary.zalandoscraper.api_telegram.service.QuickSender;
import com.vicary.zalandoscraper.exception.ScraperBotException;
import com.vicary.zalandoscraper.model.ChatNotification;
import com.vicary.zalandoscraper.service.UpdateReceiverService;
import com.vicary.zalandoscraper.service.repository_services.NotificationChatService;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class ChatNotificationSenderTest {

    @Autowired
    private ChatNotificationSender sender;

    @MockBean
    private QuickSender quickSender;

    @MockBean
    private NotificationChatService notificationChatService;

    @MockBean
    private UpdateReceiverService updateReceiverService;


    @SneakyThrows
    @Test
    void send_NormalTenChatNotifications() {
        //given
        List<ChatNotification> givenTenNotifications = getValidListOfTenNotifications();

        //when
        sender.send(givenTenNotifications);

        //then
        assertEquals(10, sender.getSentAmountAndReset());
        assertEquals(0, sender.getFailedAmount());
        verify(quickSender, times(10)).notification(any(ChatNotification.class));
        verify(notificationChatService, times(0)).saveEntity(any(ChatNotification.class));
    }

    @SneakyThrows
    @Test
    void send_NormalTenChatNotificationsAndSaveToRepository() {
        //given
        List<ChatNotification> givenTenNotifications = getValidListOfTenNotifications();

        //when
        sender.sendAndSave(givenTenNotifications);

        //then
        assertEquals(10, sender.getSentAmountAndReset());
        assertEquals(0, sender.getFailedAmount());
        verify(quickSender, times(10)).notification(any(ChatNotification.class));
        verify(notificationChatService, times(10)).saveEntity(any(ChatNotification.class));
    }

    @SneakyThrows
    @Test
    void send_NormalElevenChatNotificationsButHeFailedOne() {
        //given
        ChatNotification givenFailedNotification = new ChatNotification("1", "fail", false);
        List<ChatNotification> givenElevenNotifications = getValidListOfTenNotifications();
        givenElevenNotifications.add(givenFailedNotification);

        //when
        doThrow(ScraperBotException.class).when(quickSender).notification(givenFailedNotification);
        sender.send(givenElevenNotifications);

        //then
        assertEquals(10, sender.getSentAmountAndReset());
        assertEquals(1, sender.getFailedAmount());
        verify(quickSender, times(11)).notification(any(ChatNotification.class));
        verify(notificationChatService, times(0)).saveEntity(any(ChatNotification.class));
    }




    private List<ChatNotification> getValidListOfTenNotifications() {
        return new ArrayList<>(Arrays.asList(
                new ChatNotification("123", "text", false),
                new ChatNotification("123", "text", false),
                new ChatNotification("123", "text", false),
                new ChatNotification("123", "text", false),
                new ChatNotification("123", "text", false),
                new ChatNotification("123", "text", false),
                new ChatNotification("123", "text", false),
                new ChatNotification("123", "text", false),
                new ChatNotification("123", "text", false),
                new ChatNotification("123", "text", false)
        ));
    }
}














