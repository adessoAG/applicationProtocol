package de.adesso.example.application.marketing;

import org.javamoney.moneta.Money;

import lombok.Getter;

/**
 * This is the base class of all vouchers. Are voucher is value which may be
 * given to other people to raise the interest for our business.
 *
 * @author Matthias
 *
 */
@Getter
public abstract class Voucher {

	/** unique identifier of the voucher */
	private final String voucherId;
	/** Used vouchers cannot gain price reductions */
	private boolean isUsed = false;

	public Voucher(final String voucherId) {
		this.voucherId = voucherId;
	}

	public abstract Money calculateDiscount(Money price);

	public void setUtilized() {
		this.isUsed = true;
	}
}
