/**
 * The MIT License (MIT)
 *
 * Copyright © 2020 Matthias Brenner and Adesso SE
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the “Software”), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED “AS IS”, WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *
 */
package de.adesso.example.application.shopping;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.javamoney.moneta.Money;

import de.adesso.example.application.accounting.Customer;
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
public class ShoppingCart implements Serializable {

	private static final long serialVersionUID = 1L;

	/** entries of the shopping cart */
	private final List<ShoppingCartEntry> entries = new ArrayList<>();
	/** customer representation who is going to purchase articles */
	@Getter
	@Setter
	private Customer customer;
	/** vouchers assigned on cart level */
	@Getter
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
	 * Return all entries of the shopping cart.
	 *
	 * @return the list of the entries
	 */
	public List<ShoppingCartEntry> getAllEntries() {
		return this.entries;
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

	public String toString() {
		final StringBuilder sb = new StringBuilder();
		return this.toString(sb, 0).toString();
	}

	public StringBuilder toString(final StringBuilder sb, final int indent) {
		this.identation(sb, indent)
				.append(this.getClass().getName()).append("\n");
		this.identation(sb, indent + 1)
				.append("total: ").append(this.total.toString()).append("\n");
		this.identation(sb, indent + 1)
				.append("cart basket:\n");
		this.basket.toString(sb, indent + 2);
		this.identation(sb, indent + 1)
				.append("entries:\n");
		this.entries.forEach(e -> e.toString(sb, indent + 2));
		return sb;
	}

	private StringBuilder identation(final StringBuilder sb, final int tabs) {
		for (int i = 0; i < tabs; i++) {
			sb.append('\t');
		}
		return sb;
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

	public void clearVouchers() {
		this.basket.clear();
		this.entries.stream()
				.forEach(ShoppingCartEntry::clearVouchers);
	}

	public void splitAll() {
		this.entries.stream().forEach(ShoppingCartEntry::splitAll);
	}
}
