package de.adesso.example.application.marketing;

import org.javamoney.moneta.Money;

import de.adesso.example.framework.exception.CalculationNotApplicable;

public class VoucherNotApplicableException extends CalculationNotApplicable {

	private static final long serialVersionUID = -1911326509746932410L;

	private VoucherNotApplicableException(final String message) {
		super(message);
	}

	public static VoucherNotApplicableException lowPriceException(final Money absoluteDiscount) {
		final String message = String.format("price may not below vouchers value: %s", absoluteDiscount);
		return new VoucherNotApplicableException(message);
	}

	public static VoucherNotApplicableException wrongType(final Voucher voucher) {
		final String message = String.format("voucher is not discountable: %s", voucher);
		return new VoucherNotApplicableException(message);
	}
}
