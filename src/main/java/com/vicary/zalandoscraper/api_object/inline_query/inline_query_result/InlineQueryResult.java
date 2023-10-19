package com.vicary.zalandoscraper.api_object.inline_query.inline_query_result;

import com.vicary.zalandoscraper.api_object.ApiObject;
import com.vicary.zalandoscraper.api_request.Validation;

public interface InlineQueryResult extends ApiObject {
    String getType();
    String getId();
}
