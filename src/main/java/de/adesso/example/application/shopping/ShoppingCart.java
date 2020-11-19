package de.adesso.example.application.shopping;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.javamoney.moneta.Money;

import de.adesso.example.application.accounting.Customer;
import de.adesso.example.application.marketing.Voucher;
import de.adesso.example.application.marketing.VoucherApplication;
import de.adesso.example.application.marketing.VoucherBasket;
import de.adesso.example.application.stock.Article;
import lombok.Getter;
import lombok.Setter;

/**
 * This class represents the shopping cart which consists of several objects of
 * type {@link ShoppingCartEntry}. The class provides functionality to
 * manipulate the shopping cart.
 *
 * @author Matthias
 *
 */
public class ShoppingCart {

	/** entries of the shopping cart */
	private final List<ShoppingCartEntry> entries = new ArrayList<>();
	/** customer representation who is going to purchase articles */
	@Getter
	@Setter
	private Customer customer;
	/** vouchers assigned on cart level */
	private final VoucherBasket basket = new VoucherBasket(VoucherApplication.APPLICABLE_TO_CART);
	/** total of the cart */
	@Getter
	@Setter
	private Money total;

	/**
	 * Remove the shopping cart entry which holds the given article. If this entry
	 * does not exist, the method does not change the state and end silently.
	 *
	 * @param article the article to be removed
	 */
	public void removeEntry(final Article article) {
		final Optional<Integer> posOfArticle = this.posOfArticle(article);
		if (posOfArticle.isEmpty()) {
			return;
		}

		this.entries.remove(posOfArticle.get().intValue());
	}

	/**
	 * Reduce the number of articles within the shopping cart entry which holds the
	 * given article. If this entry does not exist, the method does not change the
	 * state and end silently. If the given count is greater or equal to the number
	 * of entries within the shopping cart entry, it is removed from the cart
	 * otherwise the count is reduced.
	 *
	 * @param article the article to be removed
	 */
	public void removeEntry(final Article article, final int count) {
		final Optional<ShoppingCartEntry> oce = this.lookupEntry(article);
		if (oce.isEmpty()) {
			return;
		}
		if (count >= oce.get().getCount()) {
			this.entries.remove(oce.get());
			return;
		}
		oce.get().add(-count);
	}

	/**
	 * Add a single article to the shopping cart. If there is no shopping cart entry
	 * containing this article, one is created and added to the cart. If it exists
	 * already, the count is increased by 1.
	 *
	 * @param article the article
	 */
	public void addEntry(final Article article) {
		this.addEntry(article, 1);
	}

	/**
	 * Add a given number of articles to the shopping cart. If there is no shopping
	 * cart entry containing this article, one is created and added to the cart. If
	 * it exists already, the count is increased by the requested number of
	 * articles.
	 *
	 * @param article the article
	 */
	public void addEntry(final Article article, final int count) {
		final Optional<ShoppingCartEntry> oce = this.lookupEntry(article);
		if (oce.isEmpty()) {
			this.entries.add(new ShoppingCartEntry(article, count));
		} else {
			oce.get().add(count);
		}
	}

	/**
	 * Return the entry containing the given article
	 *
	 * @param article the article
	 * @return the shopping cart entry
	 */
	public Optional<ShoppingCartEntry> getEntry(final Article article) {
		return this.lookupEntry(article);
	}

	/**
	 * Check the shopping cart whether it contains a specific article. Returns true
	 * if the article is present.
	 *
	 * @param article the article to look up
	 * @return true if the article exists within the cart
	 */
	public boolean contains(final Article article) {
		final Optional<ShoppingCartEntry> oce = this.lookupEntry(article);
		return !oce.isEmpty();
	}

	private Optional<Integer> posOfArticle(final Article article) {
		return this.entries.stream()
				.filter(a -> a.getArticle().equals(article))
				.map(a -> a.getPosition())
				.findFirst();
	}

	private Optional<ShoppingCartEntry> lookupEntry(final Article article) {
		return this.entries.stream()
				.filter(a -> a.getArticle().equals(article))
				.findFirst();
	}

	public void assignVouchers(final List<Voucher> vouchers) {
		this.resetTryUse(vouchers);

		// assign applicable vouchers to the cart
		this.assignAppropriateVouchersToCart(vouchers);

		// assign the remaining applicable vouchers to the entries
		this.assignAppropriateVouchersToEntries(vouchers);
	}

	private void assignAppropriateVouchersToCart(final List<Voucher> vouchers) {
		final List<Voucher> selectedVouchers = VoucherBasket.extractVouchersByApplicability(vouchers,
				VoucherApplication.APPLICABLE_TO_CART);
		selectedVouchers.stream()
				.filter(v -> this.basket.isAssignable(v))
				.forEach(v -> this.basket.addVoucher(v));
	}

	private void assignAppropriateVouchersToEntries(final List<Voucher> vouchers) {
		List<Voucher> selectedVouchers;
		selectedVouchers = VoucherBasket.extractVouchersByApplicability(vouchers,
				VoucherApplication.APPLICABLE_TO_ENTRY);
		this.entries.stream()
				.forEach(e -> e.assignVouchers(selectedVouchers));
	}

	private void resetTryUse(final List<Voucher> vouchers) {
		vouchers.stream()
				.forEach(v -> v.resetTryUse());
	}
}
