package com.hsbc.hbar.teller.broker.web.api;

import javax.validation.constraints.NotBlank;

import lombok.Data;

@Data
public class UserActionAuthorization {

	@NotBlank
	private String docType;
	
	@NotBlank
	private String docNumber;

}
