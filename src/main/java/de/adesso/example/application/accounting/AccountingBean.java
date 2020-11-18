package de.adesso.example.application.accounting;

import java.util.Optional;

import org.javamoney.moneta.Money;
import org.springframework.stereotype.Service;

import de.adesso.example.application.employment.Employee;
import de.adesso.example.application.employment.EmployeeAppendix;
import de.adesso.example.framework.ApplicationAppendix;
import de.adesso.example.framework.ApplicationProtocol;
import de.adesso.example.framework.annotation.CallStrategy;
import de.adesso.example.framework.annotation.CallingStrategy;
import de.adesso.example.framework.annotation.Required;

@Service
public class AccountingBean {

	@CallStrategy(strategy = CallingStrategy.Eager)
	public ApplicationProtocol<Money> checkOrAddCustomer(
			@Required final ApplicationProtocol<Money> state) {

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

		return state;
	}
}
