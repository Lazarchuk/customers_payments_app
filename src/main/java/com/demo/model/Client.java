package com.demo.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "clients")
@Getter
@Setter
@JsonIgnoreProperties({"hibernateLazyInitializer"})
@XmlRootElement(name = "client")
@XmlAccessorType(XmlAccessType.FIELD)
public class Client {

    public Client() {
        accounts = new ArrayList<>();
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "client_id", nullable = false)
    @JsonIgnore
    @XmlTransient
    private Integer clientId;

    @Column(name = "first_name", nullable = false)
    @JsonProperty("first_name")
    @XmlElement(name = "first_name")
    @NotNull
    private String firstName;

    @Column(name = "last_name", nullable = false)
    @JsonProperty("last_name")
    @XmlElement(name = "last_name")
    @NotNull
    private String lastName;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @XmlElements(@XmlElement(name = "account", type = Account.class))
    private List<Account> accounts;
}
