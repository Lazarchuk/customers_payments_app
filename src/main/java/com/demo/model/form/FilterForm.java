package com.demo.model.form;

import lombok.Getter;
import lombok.Setter;
import javax.validation.constraints.Min;

/**
 * Thymeleaf form
 */
@Getter
@Setter
public class FilterForm {

    @Min(value = 1, message = "Payer id must be  1 or more integer value")
    private Integer payerId;

    @Min(value = 1, message = "Recipient id must be  1 or more integer value")
    private Integer recipientId;

    @Min(value = 1, message = "Source account id must be  1 or more integer value")
    private Integer sourceAccount;

    @Min(value = 1, message = "Destination account id must be  1 or more integer value")
    private Integer destinationAccount;
}
