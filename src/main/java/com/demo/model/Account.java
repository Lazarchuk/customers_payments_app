package com.demo.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import javax.persistence.*;
import javax.xml.bind.annotation.*;

@Entity
@Table(name = "accounts")
@Getter
@Setter
@NoArgsConstructor
@XmlRootElement(name = "account")
@XmlAccessorType(XmlAccessType.FIELD)
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "account_id", nullable = false)
    private Integer accountId;

    @Column(name = "account_num", nullable = false)
    @JsonProperty("account_num")
    @XmlElement(name = "account_num")
    private String accountNumber;

    @Column(name = "account_type", nullable = false)
    @JsonProperty("account_type")
    @XmlElement(name = "account_type")
    private String accountType;

    @Column(name = "balance", nullable = false)
    private Double balance;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id")
    @JsonIgnore
    @XmlTransient
    private Client client;

}
