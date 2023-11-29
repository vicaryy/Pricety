package com.vicary.zalandoscraper.service.response.inline_markup;

import java.util.concurrent.ThreadLocalRandom;

class ThreadRandom {
    public int generate(int from, int to) {
        return ThreadLocalRandom.current().nextInt(from, to);
    }
}
