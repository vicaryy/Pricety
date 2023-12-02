package com.vicary.zalandoscraper.scraper;

import com.vicary.zalandoscraper.pattern.Pattern;

public class ScraperFactory {

    private ScraperFactory() {
    }

    public static Scraper getScraperFromLink(String link) {
        if (Pattern.isZalandoURL(link))
            return new ZalandoScraper();

        else if (Pattern.isHebeURL(link))
            return new HebeScraper();

        else if (Pattern.isNikeURL(link))
            return new NikeScraper();

        return null;
    }

    public static Scraper getScraperFromServiceName(String serviceName) {
        if (serviceName.equals("zalando.pl"))
            return new ZalandoScraper();

        else if (serviceName.equals("hebe.pl"))
            return new HebeScraper();

        else if (serviceName.equals("nike.pl"))
            return new NikeScraper();

        return null;
    }
}
