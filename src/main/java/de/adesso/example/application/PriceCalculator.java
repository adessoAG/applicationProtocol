package de.adesso.example.application;

import java.math.BigDecimal;

import de.adesso.example.framework.ApplicationProtocol;
import de.adesso.example.framework.annotation.Emulated;
import de.adesso.example.framework.annotation.Implementation;

/**
 * This interface defines the functionality required for calculation of prices.
 *
 * @author Matthias
 *
 */
@Emulated
public interface PriceCalculator {

	/**
	 *
	 * @param previousPrice
	 * @param article
	 * @return
	 */
	@Implementation()
	ApplicationProtocol<BigDecimal> calculatePrice(Article article, ApplicationProtocol<BigDecimal> appendixes);
}
