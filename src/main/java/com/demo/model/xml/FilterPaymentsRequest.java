package com.demo.model.xml;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@Getter
@Setter
@NoArgsConstructor
@XmlRootElement(name = "filter")
@XmlAccessorType(XmlAccessType.FIELD)
public class FilterPaymentsRequest {

    @JsonProperty("payer_id")
    @XmlElement(name = "payer_id")
    private Integer payerId;

    @JsonProperty("recipient_id")
    @XmlElement(name = "recipient_id")
    private Integer recipientId;

    @JsonProperty("source_acc_id")
    @XmlElement(name = "source_acc_id")
    private Integer sourceAccount;

    @JsonProperty("dest_acc_id")
    @XmlElement(name = "dest_acc_id")
    private Integer destinationAccount;
}
