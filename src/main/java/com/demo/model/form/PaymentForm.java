package com.demo.model.form;

import lombok.Getter;
import lombok.Setter;
import javax.validation.constraints.Min;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

/**
 * Thymeleaf form
 */
@Getter
@Setter
public class PaymentForm {

    @Min(value = 1, message = "Source account id must be  1 or more integer value")
    private Integer sourceAccount;

    @Min(value = 1, message = "Destination account id must be 1 or more integer value")
    private Integer destinationAccount;

    @Positive
    private Double amount;

    @Size(min=0, max=255, message = "Destination account id must less than 255 characters")
    private String reason;
}
