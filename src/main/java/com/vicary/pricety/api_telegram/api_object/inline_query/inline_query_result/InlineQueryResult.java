package com.vicary.pricety.api_telegram.api_object.inline_query.inline_query_result;

import com.vicary.pricety.api_telegram.api_object.ApiObject;

public interface InlineQueryResult extends ApiObject {
    String getType();
    String getId();
}
