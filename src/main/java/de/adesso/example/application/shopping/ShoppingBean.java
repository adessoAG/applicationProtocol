/**
 * The MIT License (MIT)
 *
 * Copyright © 2020 Matthias Brenner and Adesso SE
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the “Software”), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED “AS IS”, WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *
 */
package de.adesso.example.application.shopping;

import org.javamoney.moneta.Money;

import de.adesso.example.application.PriceCalculatorAnnotated;
import de.adesso.example.application.accounting.AccountingBean;
import de.adesso.example.application.employment.EmployeeShoppingBean;
import de.adesso.example.application.marketing.MarketingBean;
import de.adesso.example.application.stock.Article;
import de.adesso.example.framework.ApplicationProtocol;
import de.adesso.example.framework.annotation.Emulated;
import de.adesso.example.framework.annotation.Implementation;
import de.adesso.example.framework.annotation.ImplementationDefinition;
import de.adesso.example.framework.annotation.RequiredParameter;

@Emulated
public interface ShoppingBean {

	/**
	 * This calculation processes any necessary restrictions by calculating the
	 * shopping cart. If customer information is already assigned to the cart, this
	 * method does not change it. If the customer information is not yet available,
	 * it will access the appropriate information from the appendixes. The same
	 * applies to the vouchers. If they are already assigned, they will be used as
	 * assigned.
	 * <p>
	 * If the method finds additional vouchers within the appendixes, they will be
	 * assigned to the cart by best guess.
	 *
	 * @param cart       the cart containing the articles and the amount of them to
	 *                   be purchased
	 * @param appendixes the appendixes to the operation
	 * @return the results for the next step within the calculation chain
	 */
	@ImplementationDefinition(
			value = {
					@Implementation(bean = ShoppingCartCalculator.class, method = "initCartProcessing"),
					@Implementation(bean = EmployeeShoppingBean.class, method = "setEmployeeCustomer"),
					@Implementation(bean = AccountingBean.class, method = "checkOrAddCustomerOnCart"),
					@Implementation(bean = MarketingBean.class, method = "assignVouchers"),
					@Implementation(bean = ShoppingCartCalculator.class, method = "priceCartParallel")
			})
	ApplicationProtocol<ShoppingCart> priceCart(
			@RequiredParameter ShoppingCart cart,
			@RequiredParameter ApplicationProtocol<ShoppingCart> appendixes);

	@ImplementationDefinition(
			value = {
					@Implementation(bean = EmployeeShoppingBean.class, method = "setEmployeeCustomer"),
					@Implementation(bean = AccountingBean.class, method = "checkOrAddCustomer"),
					@Implementation(bean = PriceCalculatorAnnotated.class, method = "calculatePriceOfArticle")
			})
	ApplicationProtocol<Money> calculatePriceOfArticle(
			@RequiredParameter Article article,
			@RequiredParameter ApplicationProtocol<Money> appendixes);

}
