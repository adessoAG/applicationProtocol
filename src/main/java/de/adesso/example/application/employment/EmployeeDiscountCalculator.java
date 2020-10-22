package de.adesso.example.application.employment;

import java.math.BigDecimal;

import org.springframework.stereotype.Service;

import de.adesso.example.application.stock.Article;
import de.adesso.example.framework.ApplicationFrameworkInvokable;
import de.adesso.example.framework.ApplicationProtocol;

@Service
public class EmployeeDiscountCalculator
		implements ApplicationFrameworkInvokable {

	public ApplicationProtocol<BigDecimal> calculatePrice(final Article article, final Employee employee) {
		// TODO Auto-generated method stub
		return null;
	}
}
