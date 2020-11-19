package de.adesso.example.application.shopping;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import de.adesso.example.application.marketing.Voucher;
import de.adesso.example.application.marketing.VoucherApplication;
import de.adesso.example.application.marketing.VoucherBasket;
import de.adesso.example.application.stock.Article;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

/**
 * The class describes an entry in the shopping cart. It is the customer request
 * to buy an article count times.
 * <p>
 * The entry has to position information. position is the position within the
 * cart. If discounts are applied, which are only applicable to a single
 * product, and if the entry contains more than one article, the entry needs to
 * be split.
 *
 * @author Matthias
 *
 */
@Getter
@EqualsAndHashCode
public class ShoppingCartEntry {

	private final Article article;
	@Setter
	private int position;
	@Setter
	private int count;
	/**
	 * If vouchers can only be applied to a single article, the can be attached to
	 * that article. This ensures, that the voucher is used with that article the
	 * customer wants to use it.
	 */
	private final VoucherBasket basket = new VoucherBasket(VoucherApplication.APPLICABLE_TO_ENTRY);
	/**
	 * The sub-entries all belong to the same entry, thus they comprise all the same
	 * article. The number of the articles (count) has always to match the number of
	 * sub-entries.
	 * <p>
	 * Sub-entries are required, if a voucher can only be assigned to a single
	 * article. In this case, the voucher has to be assigned to the sub-entry.
	 */
	private final List<ShoppingCartSubEntry> subEntries = new ArrayList<>();

	public ShoppingCartEntry(final Article article, final int count) {
		this.article = article;
		this.count = count;
	}

	public void add(final int number) {
		this.count += number;
		this.subEntries.clear();
	}

	public void assignVouchers(final List<Voucher> selectedVouchers) {
		selectedVouchers.stream()
				.forEach(this::assignSingleVoucher);
	}

	public void resetTryUse() {
		this.basket.resetTryUse();
		this.subEntries.stream().forEach(ShoppingCartSubEntry::resetTryUse);
	}

	private void assignSingleVoucher(final Voucher voucher) {
		// first try entry
		if (voucher.getApplicableAt().contains(VoucherApplication.APPLICABLE_TO_ENTRY)
				&& this.basket.isAssignable(voucher)) {
			this.basket.addVoucher(voucher);
			return;
		}

		// try to apply to sub-entries
		if (voucher.getApplicableAt().contains(VoucherApplication.APPLICABLE_TO_SUB_ENTRY)) {
			this.splitEntries();
			this.subEntries.stream()
					.filter(se -> se.isAssignable(voucher))
					.forEach(se -> se.assignVoucher(voucher));
		}
	}

	private void splitEntries() {
		this.subEntries.clear();
		this.subEntries.addAll(IntStream.range(0, this.count)
				.mapToObj(i -> new ShoppingCartSubEntry(this, 1))
				.collect(Collectors.toList()));
	}
}
