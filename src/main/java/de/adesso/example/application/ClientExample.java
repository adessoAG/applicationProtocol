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
package de.adesso.example.application;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.javamoney.moneta.Money;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;

import de.adesso.example.application.employment.Employment;
import de.adesso.example.application.marketing.Marketing;
import de.adesso.example.application.marketing.Voucher;
import de.adesso.example.application.marketing.VoucherDiscountAbsolute;
import de.adesso.example.application.shopping.ShoppingBean;
import de.adesso.example.application.shopping.ShoppingCart;
import de.adesso.example.application.stock.Article;
import de.adesso.example.framework.ApplicationProtocol;

@Service
public class ClientExample implements CommandLineRunner {

	private final PriceCalculator manualPriceCalculator;
	private final ShoppingBean shoppingBean;

	private final Cashier cashier;

	private final Employment employment;
	private final Marketing marketing;

	@Autowired
	public ClientExample(
			final PriceCalculator priceCalculator,
			final ShoppingBean shoppingBean,
			final Cashier cashier,
			final Employment employment,
			final Marketing marketing) {
		this.manualPriceCalculator = priceCalculator;
		this.shoppingBean = shoppingBean;
		this.cashier = cashier;
		this.employment = employment;
		this.marketing = marketing;
	}

	@Override
	public void run(final String... args) throws Exception {

		this.runSimplePricing();

		this.runCartPricing();
	}

	private void runCartPricing() {
		System.out.println("calculate the whole cart -----------------------------");
		ApplicationProtocol<ShoppingCart> state = new ApplicationProtocol<>();
		state.addAllAppendixes(null, this.createCartVouchers());
		final ShoppingCart cart = this.buildShoppingCart();
		state = this.shoppingBean.priceCart(cart, state);
		System.out.println("the calculated cart is:-------------------------------");
		System.out.println(cart.toString());
		// book the last calculation
		System.out.println("the corresponding state is: --------------------------");
		System.out.println(state);
		this.cashier.encash(cart, state);
	}

	private Collection<Voucher> createCartVouchers() {
		final List<Voucher> vouchers = new ArrayList<>();
		vouchers.add(new VoucherDiscountAbsolute("AbsoluteDiscount 123456", Money.of(15.00, Standard.EUROS)));
		vouchers.add(new VoucherDiscountAbsolute("AbsoluteDiscount 123457", Money.of(15.00, Standard.EUROS)));
		vouchers.add(new VoucherDiscountAbsolute("AbsoluteDiscount 123458", Money.of(15.00, Standard.EUROS)));
		vouchers.add(new VoucherDiscountAbsolute("AbsoluteDiscount 123459", Money.of(15.00, Standard.EUROS)));
		return vouchers;
	}

	private void runSimplePricing() {
		// customer informs about the price
		System.out.println("calculate the price of a simple product with help of the manual configured calculator");
		Article article = this.customerEnteredArticle();
		ApplicationProtocol<Money> state = new ApplicationProtocol<>();
		state = this.manualPriceCalculator.calculatePrice(article, state);
		Money price = state.getResult();
		System.out.println(String.format("%s: %s", article.getArticelId(), price));
		System.out.println("The protocol is: ");
		System.out.println(state.toString());

		// customer informs about the price
		System.out
				.println("calculate the price of a simple product with help of the by annotation generated caclulator");
		article = this.customerEnteredArticle();
		state = new ApplicationProtocol<>();
		state = this.shoppingBean.calculatePriceOfArticle(article, state);
		final Money priceAnnotated = state.getResult();
		System.out.println(String.format("based on annotation: %s: %s", article.getArticelId(), priceAnnotated));
		System.out.println("The protocol is: ");
		System.out.println(state.toString());

		// employee informs about the price
		System.out.println("calculate the price of a simple product for an employee");
		article = this.customerEnteredArticle();
		state = new ApplicationProtocol<>();
		state.addAppendix(null, this.employment.createEmployee("Müller", "Hans", 1234));
		state = this.shoppingBean.calculatePriceOfArticle(article, state);
		price = state.getResult();
		System.out.println(String.format("%s: %s", article.getArticelId(), price));
		System.out.println("The protocol is: ");
		System.out.println(state.toString());

		// discount with use of a voucher
		System.out.println("calculate the price while using a voucher");
		article = this.customerEnteredArticle();
		state = new ApplicationProtocol<>();
		Voucher tenEuroDiscount = this.marketing.createTenEuroDiscount();
		state.addAppendix(null, tenEuroDiscount);
		state = this.shoppingBean.calculatePriceOfArticle(article, state);
		price = state.getResult();
		System.out.println(String.format("%s: %s", article.getArticelId(), price));
		System.out.println("The protocol is: ");
		System.out.println(state.toString());

		// discount with use of a voucher for an employee
		System.out.println("calculate the price while using a voucher for an employee");
		article = this.customerEnteredArticle();
		state = new ApplicationProtocol<>();
		tenEuroDiscount = this.marketing.createTenEuroDiscount();
		state.addAppendix(null, tenEuroDiscount);
		state.addAppendix(null, this.employment.createEmployee("Müller", "Hans", 1234));
		state = this.shoppingBean.calculatePriceOfArticle(article, state);
		price = state.getResult();
		System.out.println(String.format("%s: %s", article.getArticelId(), price));
		System.out.println("The protocol is: ");
		System.out.println(state.toString());
	}

	private ShoppingCart buildShoppingCart() {
		final ShoppingCart cart = new ShoppingCart();
		cart.addEntry(new Article("12345"));
		cart.addEntry(new Article("112244"), 3);
		cart.addEntry(new Article("112255"));
		cart.addEntry(new Article("112266"));
		cart.addEntry(new Article("112267"));
		cart.addEntry(new Article("112268"), 10);
		return cart;
	}

	private Article customerEnteredArticle() {
		return new Article("112244");
	}

}
