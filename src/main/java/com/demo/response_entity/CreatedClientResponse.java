package com.demo.response_entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CreatedClientResponse {
    @JsonProperty("client_id")
    private Integer clientId;
}
