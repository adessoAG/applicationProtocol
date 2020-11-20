package de.adesso.example.application.marketing;

import org.javamoney.moneta.Money;

public class VoucherDiscountRelative extends VoucherDiscount {

	private static final long serialVersionUID = 3074189266920702406L;
	private final int discountPercentage;

	public VoucherDiscountRelative(final String voucherId, final int discountPercentage) {
		super(voucherId, VoucherCompatibility.STAND_ALONE_WITHIN_TYPE, 1, VoucherApplication.APPLICABLE_TO_SUB_ENTRY);
		this.discountPercentage = discountPercentage;
	}

	@Override
	public Money calculateDiscount(final Money price) {
		return price.multiply(this.discountPercentage).divide(100.00);
	}
}
