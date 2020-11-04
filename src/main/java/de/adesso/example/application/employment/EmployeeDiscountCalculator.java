package de.adesso.example.application.employment;

import org.javamoney.moneta.Money;
import org.springframework.stereotype.Service;

import de.adesso.example.application.Standard;
import de.adesso.example.application.accounting.Account;
import de.adesso.example.application.accounting.AccountingRecord;
import de.adesso.example.application.accounting.AccountingRecordAppendix;
import de.adesso.example.application.accounting.CustomerAppendix;
import de.adesso.example.application.stock.Article;
import de.adesso.example.framework.ApplicationProtocol;
import de.adesso.example.framework.annotation.CallStrategy;
import de.adesso.example.framework.annotation.CallingStrategy;
import de.adesso.example.framework.annotation.Required;

@Service
public class EmployeeDiscountCalculator {

	@CallStrategy(strategy = CallingStrategy.RequiredParameters)
	public ApplicationProtocol<Money> calculatePrice(
			@Required final Article article,
			@Required final Employee employee,
			@Required final ApplicationProtocol<Money> state) {

		final Account customer = (Account) state.getAppendixOfClass(CustomerAppendix.class).get().getContent();

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
}
