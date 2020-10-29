package de.adesso.example.application.employment;

import java.math.BigDecimal;

import org.springframework.stereotype.Service;

import de.adesso.example.application.stock.Article;
import de.adesso.example.framework.ApplicationProtocol;
import de.adesso.example.framework.annotation.CallStrategy;
import de.adesso.example.framework.annotation.CallingStrategy;
import de.adesso.example.framework.annotation.Required;

@Service
public class EmployeeDiscountCalculator {

	@CallStrategy(strategy = CallingStrategy.RequiredParameters)
	public ApplicationProtocol<BigDecimal> calculatePrice(
			@Required final Article article,
			@Required final Employee employee,
			final ApplicationProtocol<BigDecimal> state) {
		// TODO Auto-generated method stub
		return null;
	}
}
