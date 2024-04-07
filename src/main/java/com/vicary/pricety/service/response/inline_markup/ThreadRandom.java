package com.vicary.pricety.service.response.inline_markup;

import java.util.concurrent.ThreadLocalRandom;

class ThreadRandom {
    int generate(int from, int to) {
        return ThreadLocalRandom.current().nextInt(from, to);
    }
}
