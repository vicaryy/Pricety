package com.vicary.zalandoscraper.service.response;

import com.vicary.zalandoscraper.ActiveUser;
import com.vicary.zalandoscraper.service.entity.AwaitedMessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AwaitedMessageResponse {

    private final AwaitedMessageService awaitedMessageService;

    public void response() {
        String userId = ActiveUser.get().getUserId();
        var awaitedMessage = awaitedMessageService.findAwaitedMessageByUserId(userId);
        awaitedMessageService.deleteAwaitedMessageById(awaitedMessage.getId());

        if (awaitedMessage.getRequest().startsWith("-edit "))

    }
}
