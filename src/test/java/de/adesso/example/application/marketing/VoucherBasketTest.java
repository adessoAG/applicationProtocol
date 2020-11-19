package de.adesso.example.application.marketing;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Set;

import org.javamoney.moneta.Money;
import org.junit.Test;

public class VoucherBasketTest {

	@Test
	public void testAddVoucherOnAppropriateLevel() {
		final VoucherBasket basket = new VoucherBasket(VoucherApplication.APPLICABLE_TO_ENTRY);
		final Voucher voucher = this.createDiscountVoucher("voucherId", VoucherCompatibility.Cooperative, 1,
				VoucherApplication.APPLICABLE_TO_ENTRY);

		basket.addVoucher(voucher);

		final Set<Voucher> vouchers = basket.getVouchers();
		assertThat(vouchers.size())
				.isEqualTo(1);
		assertThat(vouchers)
				.contains(voucher);
	}

	@Test(expected = VoucherNotUtilizableException.class)
	public void testAddVoucherOnWrongLevel() {
		final VoucherBasket basket = new VoucherBasket(VoucherApplication.APPLICABLE_TO_CART);
		final Voucher voucher = this.createDiscountVoucher("voucherId", VoucherCompatibility.Cooperative, 1,
				VoucherApplication.APPLICABLE_TO_ENTRY);

		basket.addVoucher(voucher);
	}

	@Test(expected = VoucherNotUtilizableException.class)
	public void testAddVoucherConflictsTopDog() {
		final VoucherBasket basket = new VoucherBasket(VoucherApplication.APPLICABLE_TO_CART);
		basket.addVoucher(this.createDiscountVoucher("voucherId", VoucherCompatibility.TopDog, 1,
				VoucherApplication.APPLICABLE_TO_CART));
		final Voucher voucher = this.createDiscountVoucher("voucherId", VoucherCompatibility.Cooperative, 1,
				VoucherApplication.APPLICABLE_TO_CART);

		basket.addVoucher(voucher);
	}

	@Test(expected = VoucherNotUtilizableException.class)
	public void testAddVoucherConflictsStandAlone() {
		final VoucherBasket basket = new VoucherBasket(VoucherApplication.APPLICABLE_TO_CART);
		basket.addVoucher(this.createDiscountVoucher("voucherId", VoucherCompatibility.StandAloneWithinType, 1,
				VoucherApplication.APPLICABLE_TO_CART));
		final Voucher voucher = this.createDiscountVoucher("voucherId", VoucherCompatibility.Cooperative, 1,
				VoucherApplication.APPLICABLE_TO_CART);

		basket.addVoucher(voucher);
	}

	@Test
	public void testAddVoucherDouble() {
		final VoucherBasket basket = new VoucherBasket(VoucherApplication.APPLICABLE_TO_CART);
		final Voucher voucher = this.createDiscountVoucher("voucherId", VoucherCompatibility.Cooperative, 2,
				VoucherApplication.APPLICABLE_TO_CART);

		basket.addVoucher(voucher);
		basket.addVoucher(voucher);

		assertThat(basket.getVouchers().size())
				.isEqualTo(1);
	}

	@Test
	public void testRemoverVoucher() {
		final VoucherBasket basket = new VoucherBasket(VoucherApplication.APPLICABLE_TO_CART);
		final Voucher voucher = this.createDiscountVoucher("voucherId", VoucherCompatibility.Cooperative, 2,
				VoucherApplication.APPLICABLE_TO_CART);

		basket.addVoucher(voucher);
		assertThat(basket.getVouchers().size())
				.isEqualTo(1);

		basket.removerVoucher(voucher);
		assertThat(basket.getVouchers().size())
				.isEqualTo(0);
	}

	private VoucherDiscount createDiscountVoucher(final String voucherId, final VoucherCompatibility compatibility,
			final int uses, final VoucherApplication application) {
		return new VoucherDiscount(voucherId, compatibility, uses, application) {

			@Override
			public Money calculateDiscount(final Money price) {
				// 10% discount
				return price.multiply(0.9);
			}

		};
	}
}
