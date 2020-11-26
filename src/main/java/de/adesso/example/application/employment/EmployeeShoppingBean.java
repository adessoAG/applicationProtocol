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
package de.adesso.example.application.employment;

import org.javamoney.moneta.Money;
import org.springframework.stereotype.Service;

import de.adesso.example.application.Standard;
import de.adesso.example.application.accounting.AccountingRecord;
import de.adesso.example.application.accounting.Customer;
import de.adesso.example.application.stock.Article;
import de.adesso.example.framework.ApplicationProtocol;
import de.adesso.example.framework.annotation.CallStrategy;
import de.adesso.example.framework.annotation.CallingStrategy;
import de.adesso.example.framework.annotation.Required;

@Service
public class EmployeeShoppingBean {

	@CallStrategy(strategy = CallingStrategy.REQUIRED_PARAMETER)
	public ApplicationProtocol<?> setEmployeeCustomer(
			@Required final Employee employee,
			@Required final ApplicationProtocol<?> state) {

		final Customer customer = employee.getEmployeeCustomer();
		// remove existing customer appendixes
		state.removeAll(null, Customer.class);
		state.addAppendix(null, customer);

		return state;
	}

	@CallStrategy(strategy = CallingStrategy.REQUIRED_PARAMETER)
	public ApplicationProtocol<Money> discountEmployee(
			@Required final Article article,
			@Required final Customer customer,
			@Required final Employee employee,
			@Required final ApplicationProtocol<Money> state) {

		final Money price = state.getResult();
		final Money discount = price.multiply(Standard.employeeDiscount).divide(100.0);
		state.setResult(price.subtract(discount));

		state.addAppendix(null, new EmployeeBenefit(employee, discount));
		state.addAppendix(null, AccountingRecord.builder()
				.debitor(Employment.getEmployeeDiscountCreditor())
				.creditor(customer)
				.value(discount)
				.build());

		return state;
	}
}
