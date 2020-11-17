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
import de.adesso.example.application.accounting.Customer;
import de.adesso.example.application.accounting.CustomerAppendix;
import de.adesso.example.application.employment.Employee;
import de.adesso.example.application.employment.EmployeeAppendix;
import de.adesso.example.application.shopping.ShoppingCart;
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

		final Customer customer = this.checkOrAddCustomer(state);
		this.addBookingRecords(state, price, customer);
		return state;
	}

	/**
	 * Participate within the price calculation chain. Calculates the discount for a
	 * single whole shopping cart. Creates also the accounting records within the
	 * state.
	 *
	 * @param cart     the cart to be calculated
	 * @param customer the customer
	 * @param vouchers the vouchers the customer provided
	 * @param state    state which receives the calculated cart
	 * @return
	 */
	@CallStrategy(strategy = CallingStrategy.Eager)
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

	private Customer checkOrAddCustomer(final ApplicationProtocol<Money> state) {
		boolean createCustomerAppendix = false;
		Customer customer = null;

		// if an employee is present, he will be the customer.
		final Optional<ApplicationAppendix<?>> optionalEmployeeAppendix = state
				.getAppendixOfClass(EmployeeAppendix.class);
		if (optionalEmployeeAppendix.isPresent()) {
			customer = ((Employee) optionalEmployeeAppendix.get().getContent()).getEmployeeCustomer();
			// remove existing customer appendixes
			state.removeAll(CustomerAppendix.class);
			createCustomerAppendix = true;
		} else {

			// check if customer is set explicitly
			final Optional<ApplicationAppendix<?>> customerAppendixOptional = state
					.getAppendixOfClass(CustomerAppendix.class);
			if (customerAppendixOptional.isPresent()) {
				customer = (Customer) customerAppendixOptional.get().getContent();
			} else {
				customer = Accounting.getUnknownCustomer();
				createCustomerAppendix = true;
			}
		}

		if (createCustomerAppendix) {
			state.addAppendix(new CustomerAppendix(customer));
		}

		return customer;
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
