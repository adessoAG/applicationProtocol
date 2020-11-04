package de.adesso.example.application.marketing;

import org.javamoney.moneta.Money;

import lombok.Getter;

@Getter
public abstract class Voucher {

	private final String voucherId;

	public Voucher(final String voucherId) {
		this.voucherId = voucherId;
	}

	public abstract Money calculateDiscount(Money price);
}
