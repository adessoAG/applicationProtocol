package de.adesso.example.application;

import java.math.BigDecimal;

import de.adesso.example.application.stock.Article;
import de.adesso.example.framework.ApplicationProtocol;

/**
 * This interface defines the functionality required for calculation of prices.
 *
 * @author Matthias
 *
 */
public interface PriceCalculator {

	/**
	 *
	 * @param previousPrice
	 * @param article
	 * @return
	 */
	ApplicationProtocol<BigDecimal> calculatePrice(Article article, ApplicationProtocol<BigDecimal> appendixes);
}
