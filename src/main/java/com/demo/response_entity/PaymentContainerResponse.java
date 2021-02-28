package com.demo.response_entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.xml.bind.annotation.*;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@XmlRootElement(name = "payments")
@XmlAccessorType(XmlAccessType.FIELD)
public class PaymentContainerResponse {

    @XmlElements(@XmlElement(name = "payment", type = PaymentResponse.class))
    private List<PaymentResponse> payments;
}
