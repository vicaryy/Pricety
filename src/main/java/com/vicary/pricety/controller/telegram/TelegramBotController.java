package com.vicary.pricety.controller.telegram;

import com.vicary.pricety.api_telegram.service.UpdateFetcher;
import com.vicary.pricety.service.UpdateReceiverService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/api/telegram/")
@RestController
public class TelegramBotController {

    private final UpdateReceiverService updateReceiverService;

    @PostMapping("start")
    public ResponseEntity<String> startTelegramBot() {
        UpdateFetcher.getInstance(updateReceiverService).start();
        return ResponseEntity.ok("Started.");
    }
}
