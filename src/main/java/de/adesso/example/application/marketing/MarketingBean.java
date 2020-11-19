package de.adesso.example.application.marketing;

import java.util.List;

import org.javamoney.moneta.Money;
import org.springframework.stereotype.Service;

import de.adesso.example.application.accounting.Account;
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
public class MarketingBean {

	/**
	 * Participate within the price calculation chain. Calculates the discount for a
	 * single article. Creates also the accounting records within the state.
	 *
	 * @param article  the article
	 * @param customer the customer
	 * @param voucher  the voucher to be considered
	 * @param state    the state piped through the calculation
	 * @return the original state with the reduced price set as result
	 */
	@CallStrategy(strategy = CallingStrategy.REQUIRED_PARAMETER)
	public ApplicationProtocol<Money> discountVoucher(
			@Required final Article article,
			@Required final Customer customer,
			@Required final Voucher voucher,
			@Required final ApplicationProtocol<Money> state) {

		if (voucher.getType() != VoucherType.DiscountVoucher) {
			throw VoucherNotApplicableException.wrongType(voucher);
		}
		final VoucherDiscount discountVoucher = (VoucherDiscount) voucher;
		final Money discount = discountVoucher.calculateDiscount(state.getResult());
		final Money newPrice = state.getResult().subtract(discount);
		state.setResult(newPrice);

		// add article bookkeeping record
		state.addAppendix(this.articleBookkeepingRecord(customer, state, discount));

		return state;
	}

	/**
	 * Assign the customer provided vouchers to the shopping cart as reasonable as
	 * possible.
	 *
	 * @param cart     the cart to be calculated
	 * @param vouchers the vouchers the customer provided
	 * @param state    state which receives the calculated cart
	 * @return the protocol with the updated cart as result
	 */
	@CallStrategy(strategy = CallingStrategy.REQUIRED_PARAMETER)
	public ApplicationProtocol<ShoppingCart> assignVouchers(
			@Required final ShoppingCart cart,
			@Required final List<Voucher> vouchers,
			@Required final ApplicationProtocol<ShoppingCart> state) {
		cart.assignVouchers(vouchers);

		state.setResult(cart);
		return state;
	}

	private AccountingRecordAppendix articleBookkeepingRecord(final Account customer,
			final ApplicationProtocol<Money> state,
			final Money discount) {
		return new AccountingRecordAppendix(AccountingRecord.builder()
				.debitor(Marketing.getMarketingVoucherAccount())
				.creditor(customer)
				.value(discount)
				.build());
	}
}
