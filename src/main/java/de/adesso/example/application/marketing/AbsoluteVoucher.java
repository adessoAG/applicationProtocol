package de.adesso.example.application.marketing;

import org.javamoney.moneta.Money;

public class AbsoluteVoucher extends Voucher {

	private final Money absoluteDiscount;

	public AbsoluteVoucher(final String voucherId, final Money absoluteDiscount) {
		super(voucherId);
		this.absoluteDiscount = absoluteDiscount;
	}

	@Override
	public Money calculateDiscount(final Money price) {
		if (price.isLessThan(price)) {
			throw new VoucherNotApplicableException(this.absoluteDiscount);
		}
		return this.absoluteDiscount;
	}
}
