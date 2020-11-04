package de.adesso.example.application.marketing;

import org.javamoney.moneta.Money;

public class VoucherNotApplicableException extends RuntimeException {

	private static final long serialVersionUID = -1911326509746932410L;

	public VoucherNotApplicableException(final Money absoluteDiscount) {
		super(String.format("price may not below vouchers value: %s", absoluteDiscount));
	}

}
