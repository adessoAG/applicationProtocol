package de.adesso.example.application.accounting;

import org.javamoney.moneta.Money;
import org.springframework.stereotype.Service;

import de.adesso.example.application.shopping.ShoppingCart;
import de.adesso.example.framework.ApplicationProtocol;
import de.adesso.example.framework.annotation.CallStrategy;
import de.adesso.example.framework.annotation.CallingStrategy;
import de.adesso.example.framework.annotation.Required;

@Service
public class AccountingBean {

	@CallStrategy(strategy = CallingStrategy.EAGER)
	public ApplicationProtocol<ShoppingCart> checkOrAddCustomerOnCart(
			final Customer customer,
			@Required final ApplicationProtocol<ShoppingCart> state) {
		state.getResult().setCustomer(this.checkOrSetCustomer(customer, state));

		return state;
	}

	@CallStrategy(strategy = CallingStrategy.EAGER)
	public ApplicationProtocol<Money> checkOrAddCustomer(
			final Customer customer,
			@Required final ApplicationProtocol<Money> state) {

		// check if customer is set explicitly
		this.checkOrSetCustomer(customer, state);

		return state;
	}

	private Customer checkOrSetCustomer(Customer customer, final ApplicationProtocol<?> state) {
		if (customer == null) {
			customer = Accounting.getUnknownCustomer();
			state.addAppendix(null, customer);
		}

		return customer;
	}
}
