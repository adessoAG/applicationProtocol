package de.adesso.example.application;

import org.javamoney.moneta.Money;

import de.adesso.example.application.employment.EmployeeShoppingBean;
import de.adesso.example.application.marketing.MarketingBean;
import de.adesso.example.application.stock.Article;
import de.adesso.example.application.stock.PricingBean;
import de.adesso.example.framework.ApplicationProtocol;
import de.adesso.example.framework.annotation.Emulated;
import de.adesso.example.framework.annotation.Implementation;
import de.adesso.example.framework.annotation.ImplementationDefinition;
import de.adesso.example.framework.annotation.RequiredParameter;

/**
 * This interface defines the functionality required for calculation of prices.
 * The ordering of the beans manipulating the price is essential. It can be
 * changed in this class very easily.
 *
 * @author Matthias
 *
 */
@Emulated
public interface PriceCalculatorAnnotated {

	/**
	 * Calculate the price of a single article. If there is enough information
	 * within the appendixes, the calculators may provide price reductions. This
	 * call does not take in account, if for example a voucher is applicable to only
	 * one product.
	 *
	 * @param article    the article to calculated
	 * @param appendixes the state of all appendixes traveling through the
	 *                   calculation chain
	 * @return the price for the article incorporating all price reductions
	 */
	@ImplementationDefinition(
			value = {
					@Implementation(bean = PricingBean.class, method = "buildPrice"),
					@Implementation(bean = EmployeeShoppingBean.class, method = "discountEmployee"),
					@Implementation(bean = MarketingBean.class, method = "discountVoucher")
			})
	ApplicationProtocol<Money> calculatePriceOfArticle(
			@RequiredParameter Article article,
			@RequiredParameter ApplicationProtocol<Money> appendixes);
}
