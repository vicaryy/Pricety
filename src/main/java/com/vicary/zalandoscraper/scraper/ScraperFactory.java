package com.vicary.zalandoscraper.scraper;

import com.vicary.zalandoscraper.pattern.Pattern;

public class ScraperFactory {
    public static Scraper getScraperFromLink(String link) {
        if (Pattern.isZalandoURL(link))
            return new ZalandoScraper();

        else if (Pattern.isHebeURL(link))
            return new HebeScraper();

        else if (Pattern.isNikeURL(link))
            return new NikeScraper();

        return null;
    }
}
