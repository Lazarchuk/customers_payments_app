package com.demo.response_entity;

import lombok.Getter;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

/**
 * Wrapper for XML response of filtered payments list
 */
@XmlRootElement(name = "payments")
@Getter
public class JournalContainerResponse {
    private List<JournalResponse> payments;

    @XmlElements(@XmlElement(name = "payment", type = JournalResponse.class))
    public void setPayments(List<JournalResponse> payments) {
        this.payments = payments;
    }
}
