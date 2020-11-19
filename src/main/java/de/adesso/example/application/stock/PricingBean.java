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
import de.adesso.example.application.shopping.ShoppingCart;
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

	/**
	 * Participate within the price calculation chain. Calculates the discount for a
	 * single whole shopping cart. Creates also the accounting records within the
	 * state.
	 *
	 * @param cart  the cart to be calculated
	 * @param state state which receives the calculated cart
	 * @return the calculated cart as result of the protocol
	 */
	@CallStrategy(strategy = CallingStrategy.EAGER)
	public ApplicationProtocol<ShoppingCart> calculatePriceOfCart(
			@Required final ShoppingCart cart,
			@Required final ApplicationProtocol<ShoppingCart> state) {

		state.setResult(cart);
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
