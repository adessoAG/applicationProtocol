package de.adesso.example.application.stock;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import javax.annotation.PostConstruct;

import org.javamoney.moneta.Money;
import org.springframework.stereotype.Service;

import de.adesso.example.application.Standard;
import de.adesso.example.application.accounting.Accounting;
import de.adesso.example.application.accounting.AccountingRecord;
import de.adesso.example.application.accounting.AccountingRecordAppendix;
import de.adesso.example.application.accounting.Creditor;
import de.adesso.example.application.accounting.CustomerAppendix;
import de.adesso.example.application.accounting.Debitor;
import de.adesso.example.framework.ApplicationAppendix;
import de.adesso.example.framework.ApplicationProtocol;
import de.adesso.example.framework.annotation.CallStrategy;
import de.adesso.example.framework.annotation.CallingStrategy;
import de.adesso.example.framework.annotation.Required;

@Service
public class BasePriceCalculator {

	private final Map<String, Money> articlePrices = new HashMap<>();

	@PostConstruct
	public void init() {
		this.articlePrices.put("12345", Money.of(123.50, Standard.EUROS));
		this.articlePrices.put("112244", Money.of(64.00, Standard.EUROS));
		this.articlePrices.put("112255", Money.of(89.95, Standard.EUROS));
		this.articlePrices.put("112266", Money.of(100.00, Standard.EUROS));
	}

	@CallStrategy(strategy = CallingStrategy.Eager)
	public ApplicationProtocol<Money> calculatePrice(
			@Required final Article article,
			@Required final ApplicationProtocol<Money> state) {

		final Money price = this.buildPrice(article);
		state.setResult(price);

		this.addBookingRecords(state, price);
		return state;
	}

	private Money buildPrice(final Article article) {
		final Money price = this.articlePrices.get(article.articelId);
		if (price == null) {
			throw new ArticleNotFoundException(article.getArticelId());
		}
		return price;
	}

	private ApplicationProtocol<Money> addBookingRecords(final ApplicationProtocol<Money> state, final Money price) {
		final Optional<ApplicationAppendix<?>> customerAppendixOptional = state
				.getAppendixOfClass(CustomerAppendix.class);
		Debitor customer;
		if (customerAppendixOptional.isEmpty()) {
			customer = Accounting.getUnknownCustomer();
			state.addAppendix(new CustomerAppendix(customer));
		} else {
			customer = (Debitor) customerAppendixOptional.get().getContent();
		}
		final Creditor revenueAccount = Accounting.getRevenueAccount();

		return state.addAppendix(new AccountingRecordAppendix(
				AccountingRecord.builder()
						.debitor(customer)
						.creditor(revenueAccount)
						.value(price)
						.build()));
	}
}
