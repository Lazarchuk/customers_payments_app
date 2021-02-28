package com.demo.response_entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
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
@AllArgsConstructor
@XmlRootElement(name = "created_client")
@XmlAccessorType(XmlAccessType.FIELD)
public class CreatedClientResponse {
    @JsonProperty("client_id")
    @XmlElement(name = "client_id")
    private Integer clientId;
}
