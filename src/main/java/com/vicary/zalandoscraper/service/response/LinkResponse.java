package com.vicary.zalandoscraper.service.response;

import com.vicary.zalandoscraper.api_object.Update;
import com.vicary.zalandoscraper.service.Scraper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LinkResponse {

    private final Scraper scraper;

    public void response(String URL) {
        List<String> sizes = scraper.getSizes(URL);


    }
}
