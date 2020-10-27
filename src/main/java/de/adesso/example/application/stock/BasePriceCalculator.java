package de.adesso.example.application.stock;

import java.math.BigDecimal;

import org.springframework.stereotype.Service;

import de.adesso.example.framework.ApplicationProtocol;

@Service
public class BasePriceCalculator {

	public ApplicationProtocol<BigDecimal> calculatePrice(final Article article) {

		final ApplicationProtocol<BigDecimal> protocol = new ApplicationProtocol<>();

		return protocol.setResult(new BigDecimal("7.85"));
	}
}
