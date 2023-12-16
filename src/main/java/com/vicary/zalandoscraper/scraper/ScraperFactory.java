package com.vicary.zalandoscraper.scraper;

import com.vicary.zalandoscraper.pattern.Pattern;

import java.util.Optional;

public class ScraperFactory {

    private ScraperFactory() {
    }

    public static Optional<Scraper> getScraperFromLink(String link) {
        if (Pattern.isZalandoURL(link))
            return Optional.of(new ZalandoScraper());

        else if (Pattern.isHebeURL(link))
            return Optional.of(new HebeScraper());

        else if (Pattern.isNikeURL(link))
            return Optional.of(new NikeScraper());

        else if (Pattern.isHouseURL(link))
            return Optional.of(new HouseScraper());

        return Optional.empty();
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
