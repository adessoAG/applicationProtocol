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
package de.adesso.example.application.stock;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.javamoney.moneta.Money;
import org.springframework.stereotype.Service;

import de.adesso.example.application.Standard;
import de.adesso.example.application.accounting.Accounting;
import de.adesso.example.application.accounting.AccountingRecord;
import de.adesso.example.application.accounting.AccountingRecordAppendix;
import de.adesso.example.application.accounting.Creditor;
import de.adesso.example.application.accounting.Customer;
import de.adesso.example.framework.ApplicationProtocol;
import de.adesso.example.framework.annotation.CallStrategy;
import de.adesso.example.framework.annotation.CallingStrategy;
import de.adesso.example.framework.annotation.Required;

@Service
public class PricingBean {

	private final Map<String, Money> articlePrices = new HashMap<>();

	@PostConstruct
	public void init() {
		this.articlePrices.put("12345", Money.of(123.50, Standard.EUROS));
		this.articlePrices.put("112244", Money.of(64.00, Standard.EUROS));
		this.articlePrices.put("112255", Money.of(89.95, Standard.EUROS));
		this.articlePrices.put("112266", Money.of(100.00, Standard.EUROS));
		this.articlePrices.put("112267", Money.of(10.00, Standard.EUROS));
		this.articlePrices.put("112268", Money.of(1.00, Standard.EUROS));
	}

	@CallStrategy(strategy = CallingStrategy.EAGER)
	public ApplicationProtocol<Money> buildPrice(
			@Required final Article article,
			@Required final Customer customer,
			@Required final ApplicationProtocol<Money> state) {

		final Money price = this.buildPrice(article);
		state.setResult(price);

		this.addBookingRecords(state, price, customer);
		return state;
	}

	private Money buildPrice(final Article article) {
		final Money price = this.articlePrices.get(article.articelId);
		if (price == null) {
			throw new ArticleNotFoundException(article.getArticelId());
		}
		return price;
	}

	private ApplicationProtocol<Money> addBookingRecords(final ApplicationProtocol<Money> state, final Money price,
			final Customer customer) {
		final Creditor revenueAccount = Accounting.getRevenueAccount();

		return state.addAppendix(new AccountingRecordAppendix(
				AccountingRecord.builder()
						.debitor(customer)
						.creditor(revenueAccount)
						.value(price)
						.build()));
	}
}
