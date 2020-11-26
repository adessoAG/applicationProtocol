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
				.getAppendixOfClassT(CustomerAppendix.class);
		if (customerAppendixOptional.isPresent()) {
			customer = customerAppendixOptional.get().getContent();
		} else {
			customer = Accounting.getUnknownCustomer();
			state.addAppendix(new CustomerAppendix(customer));
		}

		return customer;
	}
}
