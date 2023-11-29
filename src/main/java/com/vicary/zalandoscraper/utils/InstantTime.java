package com.vicary.zalandoscraper.utils;

import java.time.Instant;

class InstantTime {

    public Long nowInSeconds() {
        return Instant.now().getEpochSecond();
    }
}
