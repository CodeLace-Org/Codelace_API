package com.codelace.codelace.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentRequestDTO {
	private Long student;
	private String cardNumber;
	private String cardHolderName;
	private String cardExpirationDate;
	private String cardSecurityCode;
	private String email;
	private Long plan;
}
