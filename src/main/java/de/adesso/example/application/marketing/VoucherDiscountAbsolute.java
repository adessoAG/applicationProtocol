package de.adesso.example.application.marketing;

import org.javamoney.moneta.Money;

public class VoucherDiscountAbsolute extends VoucherDiscount {

	private static final long serialVersionUID = 8422108941614967498L;
	private final Money absoluteDiscount;

	public VoucherDiscountAbsolute(final String voucherId, final Money absoluteDiscount) {
		super(voucherId, VoucherCompatibility.STAND_ALONE_WITHIN_TYPE, 1, VoucherApplication.APPLICABLE_TO_SUB_ENTRY);
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
