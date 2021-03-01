package com.demo.model.form;

import lombok.Getter;
import lombok.Setter;
import javax.validation.constraints.Size;

@Getter
@Setter
public class ClientForm {

	@Size(min=2, max=40, message = "First name must be from 2 to 40 characters length")
	private String firstName;

	@Size(min=2, max = 40, message = "Last name must be from 2 to 40 characters length")
	private String lastName;

}
