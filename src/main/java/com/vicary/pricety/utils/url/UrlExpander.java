package com.vicary.pricety.utils.url;

import com.google.common.base.Preconditions;
import org.apache.http.Header;
import org.apache.http.HttpHeaders;
import org.apache.http.client.methods.HttpHead;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
class UrlExpander {

    Optional<String> expand(String URL) {
        CloseableHttpClient httpClient = HttpClientBuilder.create().disableRedirectHandling().build();
        try {
            HttpHead request = new HttpHead(URL);
            String expandedURL = httpClient.execute(request, response -> {
                final int statusCode = response.getStatusLine().getStatusCode();
                if (statusCode != 301 && statusCode != 302) {
                    return URL;
                }
                final Header[] headers = response.getHeaders(HttpHeaders.LOCATION);
                Preconditions.checkState(headers.length == 1);

                return headers[0].getValue();
            });

            if (expandedURL.equals(URL))
                return Optional.empty();

            return Optional.of(expandedURL);

        } catch (Exception uriEx) {
            return Optional.empty();
        }
    }
}
