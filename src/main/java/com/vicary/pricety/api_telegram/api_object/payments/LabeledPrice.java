package com.vicary.pricety.api_telegram.api_object.payments;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.vicary.pricety.api_telegram.api_object.ApiObject;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LabeledPrice implements ApiObject {
    /**
     * This object represents a portion of the price for goods or services.
     *
     * @param label   Portion label
     * @param amount  Price of the product in the smallest units of the currency (integer, not float/double). For example, for a price of US$ 1.45 pass amount = 145.
     */
    @JsonProperty("label")
    private String label;

    @JsonProperty("amount")
    private Integer amount;
}
