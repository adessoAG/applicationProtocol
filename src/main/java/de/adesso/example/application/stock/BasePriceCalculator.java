package de.adesso.example.application.stock;

import java.math.BigDecimal;

import org.springframework.stereotype.Service;

import de.adesso.example.framework.ApplicationProtocol;
import de.adesso.example.framework.annotation.CallStrategy;
import de.adesso.example.framework.annotation.CallingStrategy;
import de.adesso.example.framework.annotation.Required;

@Service
public class BasePriceCalculator {

	@CallStrategy(strategy = CallingStrategy.Eager)
	public ApplicationProtocol<BigDecimal> calculatePrice(
			@Required final Article article,
			@Required final ApplicationProtocol<BigDecimal> state) {

		final ApplicationProtocol<BigDecimal> protocol = new ApplicationProtocol<>();

		return protocol.setResult(new BigDecimal("7.85"));
	}
}
