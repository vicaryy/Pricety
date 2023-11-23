package com.vicary.zalandoscraper.scraper;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

class PageHTML {

    static class Nike {
        static String getValidLinkButWithoutProduct() {
            try {
                return Files.readString(Path.of("src/test/java/com/vicary/zalandoscraper/scraper/validLinkButWithoutProduct.txt"));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    static class Zalando {
        static String getTest() {
            try {
                return Files.readString(Path.of("src/test/java/com/vicary/zalandoscraper/scraper/zalando.txt"));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
