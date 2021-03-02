package com.demo.model.xml;

import com.demo.model.Payment;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import javax.xml.bind.annotation.*;
import java.util.List;

/**
 * Wrapper for list of input payments, in XML view
 */
@Getter
@Setter
@NoArgsConstructor
@XmlRootElement(name = "payments")
@XmlAccessorType(XmlAccessType.FIELD)
public class PaymentsXmlContainer {

    @XmlElements(@XmlElement(name = "payment", type = Payment.class))
    private List<Payment> payments;
}
