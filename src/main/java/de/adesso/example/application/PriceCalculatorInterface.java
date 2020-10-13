package de.adesso.example.application;

import java.math.BigDecimal;

import de.adesso.example.framework.ApplicationProtocol;

/**
 * Functional interface which defines the price calculators which may be
 * combined to build the PriceCalculator
 *
 * @author Matthias
 *
 */
@FunctionalInterface
public interface PriceCalculatorInterface {
	/**
	 *
	 * @param previousPrice
	 * @param article
	 * @return
	 */
	ApplicationProtocol<BigDecimal> calculatePrice(Article article, ApplicationProtocol<BigDecimal> appendixes);
}
