package de.adesso.example.application;

import org.javamoney.moneta.Money;

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
	ApplicationProtocol<Money> calculatePrice(Article article, ApplicationProtocol<Money> appendixes);
}
