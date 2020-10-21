package de.adesso.example.application;

import java.math.BigDecimal;

import de.adesso.example.application.stock.Article;
import de.adesso.example.framework.ApplicationProtocol;
import de.adesso.example.framework.annotation.Emulated;
import de.adesso.example.framework.annotation.Implementation;
import de.adesso.example.framework.annotation.MatchingStrategy;

/**
 * This interface defines the functionality required for calculation of prices.
 *
 * @author Matthias
 *
 */
@Emulated
public interface PriceCalculatorAnnotated {

	/**
	 *
	 * @param previousPrice
	 * @param article
	 * @return
	 */
	@Implementation(implementations = {
			"BasePriceCalculator",
			"EmployeeDiscountCalculator",
			"VoucherDiscountCalculator"
	}, strategy = { MatchingStrategy.ByType,
			MatchingStrategy.FromAppendix,
			MatchingStrategy.ByName })
	ApplicationProtocol<BigDecimal> calculatePrice(Article article, ApplicationProtocol<BigDecimal> appendixes);
}
