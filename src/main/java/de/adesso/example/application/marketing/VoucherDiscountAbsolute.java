package de.adesso.example.application.marketing;

import org.javamoney.moneta.Money;

public class VoucherDiscountAbsolute extends VoucherDiscount {

	private final Money absoluteDiscount;

	public VoucherDiscountAbsolute(final String voucherId, final Money absoluteDiscount) {
		super(voucherId, VoucherCompatibility.StandAloneWithinType, 1, VoucherApplication.ApplicableToSubEntry);
		this.absoluteDiscount = absoluteDiscount;
	}

	@Override
	public Money calculateDiscount(final Money price) {
		if (price.isLessThan(price)) {
			throw VoucherNotApplicableException.lowPriceException(this.absoluteDiscount);
		}
		return this.absoluteDiscount;
	}
}
