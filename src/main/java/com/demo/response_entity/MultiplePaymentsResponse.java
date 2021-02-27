package com.demo.response_entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class MultiplePaymentsResponse {

    public MultiplePaymentsResponse(Integer paymentId, String status){
        this.paymentId = paymentId;
        this.status = status;
    }

    @JsonProperty("payment_id")
    private Integer paymentId;
    private String status;
}
