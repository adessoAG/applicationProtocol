package de.adesso.example.application.accounting;

import java.util.Optional;

import org.javamoney.moneta.Money;
import org.springframework.stereotype.Service;

import de.adesso.example.application.shopping.ShoppingCart;
import de.adesso.example.framework.ApplicationAppendix;
import de.adesso.example.framework.ApplicationProtocol;
import de.adesso.example.framework.annotation.CallStrategy;
import de.adesso.example.framework.annotation.CallingStrategy;
import de.adesso.example.framework.annotation.Required;

@Service
public class AccountingBean {

	@CallStrategy(strategy = CallingStrategy.EAGER)
	public ApplicationProtocol<ShoppingCart> checkOrAddCustomerOnCart(
			@Required final ApplicationProtocol<ShoppingCart> state) {
		final Customer customer = this.checkOrSetCustomer(state);
		state.getResult().setCustomer(customer);

		return state;
	}

	@CallStrategy(strategy = CallingStrategy.EAGER)
	public ApplicationProtocol<Money> checkOrAddCustomer(
			@Required final ApplicationProtocol<Money> state) {

		// check if customer is set explicitly
		this.checkOrSetCustomer(state);

		return state;
	}

	private Customer checkOrSetCustomer(final ApplicationProtocol<?> state) {
		Customer customer;
		final Optional<ApplicationAppendix<Customer>> customerAppendixOptional = state
				.getAppendixOfClass(CustomerAppendix.class);
		if (customerAppendixOptional.isPresent()) {
			customer = customerAppendixOptional.get().getContent();
		} else {
			customer = Accounting.getUnknownCustomer();
			state.addAppendix(new CustomerAppendix(customer));
		}

		return customer;
	}
}
