package com.vicary.pricety.utils;

import java.time.Instant;

class InstantTime {

    public Long nowInSeconds() {
        return Instant.now().getEpochSecond();
    }
}
