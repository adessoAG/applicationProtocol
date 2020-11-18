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
	 * demonstration of a method which is build manually by the configuration
	 *
	 * @param article    article to be calculated
	 * @param appendixes the list of appendixes which may carry information not
	 *                   visible to all participating beans
	 * @return the calculated price which is based on information within appendixes
	 */
	ApplicationProtocol<Money> calculatePrice(Article article, ApplicationProtocol<Money> appendixes);
}
