package com.vicary.zalandoscraper.api_telegram.api_object.payments;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.vicary.zalandoscraper.api_telegram.api_object.ApiObject;
import com.vicary.zalandoscraper.api_telegram.api_object.User;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@EqualsAndHashCode
public class ShippingQuery implements ApiObject {
    /**
     * This object contains information about an incoming shipping query.
     *
     * @param id                Unique query identifier
     * @param from              User who sent the query
     * @param invoicePayload    Bot specified invoice payload
     * @param shippingAddress   User specified shipping address
     */
    @JsonProperty("id")
    private String id;

    @JsonProperty("from")
    private User from;

    @JsonProperty("invoice_payload")
    private String invoicePayload;

    @JsonProperty("shipping_address")
    private ShippingAddress shippingAddress;

    private ShippingQuery() {
    }
}
