package com.sample.codes.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import lombok.Data;

@Data
public class PaymentResponse {
    @JsonProperty(value = "id")
    private Long id;

    @JsonProperty(value = "order_id")
    private String orderId;

    @JsonProperty(value = "amount")
    private int amount;

    @JsonProperty(value = "key_id")
    private String keyId;

    @JsonProperty(value = "form_entity_id")
    private Long formEntityId;
}
