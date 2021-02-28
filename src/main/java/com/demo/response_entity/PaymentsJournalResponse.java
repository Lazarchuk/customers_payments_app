package com.demo.response_entity;

import com.demo.model.Client;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
public class PaymentsJournalResponse {

    @JsonProperty("payment_id")
    private Integer paymentId;

    @JsonProperty("src_acc_num")
    private String sourceAccount;

    @JsonProperty("dest_acc_num")
    private String destinationAccount;

    private BigDecimal amount;
    private ClientResponse payer;
    private ClientResponse recipient;

    public void setPayer(Client payer) {
        this.payer = new ClientResponse(payer.getFirstName(), payer.getLastName());
    }

    public void setRecipient(Client recipient) {
        this.recipient = new ClientResponse(recipient.getFirstName(), recipient.getLastName());
    }
}