package com.demo.model.form;

import lombok.Getter;
import lombok.Setter;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;

@Getter
@Setter
public class AccountForm {

	@Size(min=2, max=10, message = "Account number must be from 2 to 10 characters length")
	private String accountNumber;

	@Size(min=2, max=15, message = "Account type must be from 2 to 15 characters length")
	private String accountType;

	@PositiveOrZero(message = "Balance must be positive or zero")
	private Double balance;
}
