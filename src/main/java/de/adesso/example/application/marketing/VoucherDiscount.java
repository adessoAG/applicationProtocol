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
public abstract class VoucherDiscount extends Voucher {

	private static final long serialVersionUID = -6289942538060231381L;

	public VoucherDiscount(final String voucherId, final VoucherCompatibility compatibility) {
		super(voucherId, compatibility, VoucherType.DISCOUNT_VOUCHER);
	}

	public VoucherDiscount(final String voucherId, final VoucherCompatibility compatibility,
			final int maxApplications) {
		super(voucherId, compatibility, VoucherType.DISCOUNT_VOUCHER, maxApplications);
	}

	public VoucherDiscount(final String voucherId, final VoucherCompatibility compatibility,
			final VoucherApplication... applicableAt) {
		super(voucherId, compatibility, VoucherType.DISCOUNT_VOUCHER, applicableAt);
	}

	public VoucherDiscount(final String voucherId, final VoucherCompatibility compatibility, final int maxApplications,
			final VoucherApplication... applicableAt) {
		super(voucherId, compatibility, VoucherType.DISCOUNT_VOUCHER, maxApplications, applicableAt);
	}

	public abstract Money calculateDiscount(Money price);
}
