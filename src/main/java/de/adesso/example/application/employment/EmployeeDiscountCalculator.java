package de.adesso.example.application.employment;

import org.javamoney.moneta.Money;
import org.springframework.stereotype.Service;

import de.adesso.example.application.Standard;
import de.adesso.example.application.accounting.AccountingRecord;
import de.adesso.example.application.accounting.AccountingRecordAppendix;
import de.adesso.example.application.accounting.Customer;
import de.adesso.example.application.shopping.ShoppingCart;
import de.adesso.example.application.stock.Article;
import de.adesso.example.framework.ApplicationProtocol;
import de.adesso.example.framework.annotation.CallStrategy;
import de.adesso.example.framework.annotation.CallingStrategy;
import de.adesso.example.framework.annotation.Required;

@Service
public class EmployeeDiscountCalculator {

	@CallStrategy(strategy = CallingStrategy.RequiredParameters)
	public ApplicationProtocol<Money> discountEmployee(
			@Required final Article article,
			@Required final Customer customer,
			@Required final Employee employee,
			@Required final ApplicationProtocol<Money> state) {

		final Money price = state.getResult();
		final Money discount = price.multiply(Standard.employeeDiscount).divide(100.0);
		state.setResult(price.subtract(discount));

		state.addAppendix(new EmployeeBenefitAppendix(new EmployeeBenefit(employee, discount)));
		state.addAppendix(new AccountingRecordAppendix(AccountingRecord.builder()
				.debitor(Employment.getEmployeeDiscountCreditor())
				.creditor(customer)
				.value(discount)
				.build()));

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
	@CallStrategy(strategy = CallingStrategy.RequiredParameters)
	public ApplicationProtocol<ShoppingCart> calculatePriceOfCart(
			@Required final ShoppingCart cart,
			@Required final Employee employee,
			@Required final ApplicationProtocol<ShoppingCart> state) {

		state.setResult(cart);
		return state;
	}
}
