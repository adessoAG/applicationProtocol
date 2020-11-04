package de.adesso.example.application.marketing;

import org.javamoney.moneta.Money;
import org.springframework.stereotype.Service;

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
public class VoucherDiscountCalculator {

	@CallStrategy(strategy = CallingStrategy.RequiredParameters)
	public ApplicationProtocol<Money> calculatePrice(
			@Required final Article article,
			@Required final Voucher voucher,
			final ApplicationProtocol<Money> state) {

		final Account customer = (Account) state.getAppendixOfClass(CustomerAppendix.class).get().getContent();

		final Money discount = voucher.calculateDiscount(state.getResult());
		final Money newPrice = state.getResult().subtract(discount);
		state.setResult(newPrice);

		state.addAppendix(new AccountingRecordAppendix(AccountingRecord.builder()
				.debitor(Marketing.getMarketingVoucherAccount())
				.creditor(customer)
				.value(discount)
				.build()));

		return state;
	}
}
