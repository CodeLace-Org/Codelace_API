package com.codelace.codelace.service.external;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.codelace.codelace.model.dto.PaymentRequestDTO;
import com.codelace.codelace.model.entity.Plan;
import com.codelace.codelace.repository.PlanRepository;

@ExtendWith(MockitoExtension.class)
public class PaymentServiceExternalTest {
	@Mock
	private PlanRepository planRepository;

	@InjectMocks
	private PaymentServiceExternal paymentServiceExternal;
	@Test
	public void testProcessPaymentSuccess() {
		// Arrange
		PaymentRequestDTO paymentRequestDTO = new PaymentRequestDTO();
		paymentRequestDTO.setPlan(1L);
		paymentRequestDTO.setCardNumber("1234567890123456");
		paymentRequestDTO.setCardExpirationDate("12/24");
		paymentRequestDTO.setCardSecurityCode("123");

		Plan plan = new Plan();
		plan.setId(1L);
		plan.setPrice(BigDecimal.valueOf(10.0));

		when(planRepository.findById(1L)).thenReturn(Optional.of(plan));

		// Act
		boolean paymentProcessed = paymentServiceExternal.processPayment(paymentRequestDTO);

		// Assert
		assertTrue(paymentProcessed);
	}

	@Test
	public void testProcessPaymentInsufficientBalance() {
		// Arrange
		PaymentRequestDTO paymentRequestDTO = new PaymentRequestDTO();
		paymentRequestDTO.setPlan(1L);
		paymentRequestDTO.setCardNumber("1234567890123456");
		paymentRequestDTO.setCardExpirationDate("12/24");
		paymentRequestDTO.setCardSecurityCode("123");

		Plan plan = new Plan();
		plan.setId(1L);
		plan.setPrice(BigDecimal.valueOf(200.0)); // Set a high price to ensure insufficient balance

		when(planRepository.findById(1L)).thenReturn(Optional.of(plan));

		// Act
		boolean paymentProcessed = paymentServiceExternal.processPayment(paymentRequestDTO);

		// Assert
		assertFalse(paymentProcessed);
	}
}
