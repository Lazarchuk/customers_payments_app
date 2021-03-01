package com.demo.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import javax.persistence.*;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@Table(name = "payments")
@Getter
@Setter
@NoArgsConstructor
@XmlRootElement(name = "payment")
@XmlAccessorType(XmlAccessType.FIELD)
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "payment_id", nullable = false)
    private Integer paymentId;

    @Column(name = "source_acc_id", nullable = false)
    @JsonProperty("source_acc_id")
    @XmlElement(name = "source_acc_id")
    private Integer sourceAccount;

    @Column(name = "dest_acc_id", nullable = false)
    @JsonProperty("dest_acc_id")
    @XmlElement(name = "dest_acc_id")
    private Integer destinationAccount;

    @Column(name = "amount", nullable = false)
    private Double amount;

    @Column(name = "reason", nullable = false)
    private String reason;
}
