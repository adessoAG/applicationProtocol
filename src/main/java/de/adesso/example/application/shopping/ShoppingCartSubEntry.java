package de.adesso.example.application.shopping;

import de.adesso.example.application.marketing.Voucher;
import de.adesso.example.application.marketing.VoucherApplication;
import de.adesso.example.application.marketing.VoucherBasket;
import lombok.Getter;

/**
 * The customer is allowed to use several vouchers per article. Some vouchers
 * are limited to only one product. In this case it is necessary to split the
 * entry into sub-entries, if the customer wants to purchase several same
 * articles.
 *
 * @author Matthias
 *
 */
@Getter
public class ShoppingCartSubEntry {

	private final VoucherBasket basket = new VoucherBasket(VoucherApplication.ApplicableToSubEntry);
	private final int number;

	public ShoppingCartSubEntry(final int number) {
		this.number = number;
	}

	public void addVoucher(final Voucher voucher) {
		this.basket.addVoucher(voucher);
	}

	public void removeVoucher(final Voucher voucher) {
		this.basket.removerVoucher(voucher);
	}

	public boolean isAssignable(final Voucher voucher) {
		return this.basket.isAssignable(voucher);
	}

	public void assignVoucher(final Voucher voucher) {
		this.basket.addVoucher(voucher);
	}
}
