package com.codelace.codelace.service.external;

import java.math.BigDecimal;
import java.util.Random;

import org.springframework.stereotype.Service;

import com.codelace.codelace.exception.ResourceNotFoundException;
import com.codelace.codelace.model.dto.PaymentRequestDTO;
import com.codelace.codelace.repository.PlanRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class PaymentServiceExternal {

	// private static BigDecimal AVAILABLE_BALANCE = BigDecimal.valueOf(50.0);
	private final PlanRepository planRepository;

	public boolean processPayment(PaymentRequestDTO paymentRequestDTO) {
		BigDecimal paymentAmount = planRepository.findById(paymentRequestDTO.getPlan())
				.orElseThrow(() -> new ResourceNotFoundException("Plan not found")).getPrice();
		BigDecimal availableBalance = getBalance(paymentRequestDTO);
		boolean paymentProcessed = false;
		if (availableBalance.compareTo(paymentAmount) >= 0) {
			availableBalance = availableBalance.subtract(paymentAmount);
			paymentProcessed = true;
		}
		return paymentProcessed;
	}

	private BigDecimal getBalance(PaymentRequestDTO paymentRequestDTO) {
		String cardNumber = paymentRequestDTO.getCardNumber();
		String cardExpirationDate = paymentRequestDTO.getCardExpirationDate();
		String cardSecurityCode = paymentRequestDTO.getCardSecurityCode();

		// Concatenar los valores de los atributos
		String concat = cardNumber + cardExpirationDate + cardSecurityCode;

		// Obtener el hash code del string concatenado
		int hashCode = concat.hashCode();

		// Usar el hash code como semilla para un objeto Random
		Random random = new Random(hashCode);

		// Generar un número aleatorio entre 5 y 100
		int randomBalance = 5 + random.nextInt(96); // 5 + (0 a 95)

		// Convertir el número aleatorio a BigDecimal
		return BigDecimal.valueOf(randomBalance);
	}

}
