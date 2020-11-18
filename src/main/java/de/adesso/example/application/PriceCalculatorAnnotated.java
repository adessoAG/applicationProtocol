package de.adesso.example.application;

import org.javamoney.moneta.Money;

import de.adesso.example.application.accounting.AccountingBean;
import de.adesso.example.application.employment.EmployeeDiscountCalculator;
import de.adesso.example.application.marketing.VoucherDiscountCalculator;
import de.adesso.example.application.shopping.ShoppingCart;
import de.adesso.example.application.stock.Article;
import de.adesso.example.application.stock.PricingBean;
import de.adesso.example.framework.ApplicationProtocol;
import de.adesso.example.framework.annotation.Emulated;
import de.adesso.example.framework.annotation.Implementation;
import de.adesso.example.framework.annotation.ImplementationDefinition;
import de.adesso.example.framework.annotation.Required;
import de.adesso.example.framework.annotation.RequiredParameter;

/**
 * This interface defines the functionality required for calculation of prices.
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
	@Implementation(bean = AccountingBean.class, method = "checkOrAddCustomer")
	@Implementation(bean = PricingBean.class, method = "buildPrice")
	@Implementation(bean = EmployeeDiscountCalculator.class, method = "discountEmployee")
	@Implementation(bean = VoucherDiscountCalculator.class, method = "discountVoucher")
	ApplicationProtocol<Money> calculatePrice(
			@RequiredParameter Article article,
			@RequiredParameter ApplicationProtocol<Money> appendixes);

	/**
	 * This calculation processes any necessary restrictions by calculating the
	 * shopping cart. This method will not access vouchers from the appendixes, they
	 * already have to be assigned within the cart.
	 *
	 * @param cart       the cart containing the articles and the amount of them to
	 *                   be purchased
	 * @param appendixes the appendixes to the operation
	 * @return the results for the next step within the calculation chain
	 */
	@Implementation(bean = PricingBean.class)
	@Implementation(bean = EmployeeDiscountCalculator.class)
	@Implementation(bean = VoucherDiscountCalculator.class)
	ApplicationProtocol<ShoppingCart> calculatePriceOfCart(
			@RequiredParameter ShoppingCart cart,
			@RequiredParameter ApplicationProtocol<ShoppingCart> appendixes);

	/**
	 * Assign the customer provided vouchers to the shopping cart as reasonable as
	 * possible.
	 *
	 * @param cart  the cart to be calculated
	 * @param state state which receives the calculated cart
	 * @return the price for the whole shopping cart
	 */

	@ImplementationDefinition(value = @Implementation(bean = VoucherDiscountCalculator.class))
	public ApplicationProtocol<ShoppingCart> assignVouchers(
			@Required final ShoppingCart cart,
			@Required final ApplicationProtocol<ShoppingCart> state);
}
