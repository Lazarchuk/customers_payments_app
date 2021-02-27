package com.demo.response_entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CreatedPaymentResponse {
    @JsonProperty("payment_id")
    private Integer paymentId;
}
