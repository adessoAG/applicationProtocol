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

import org.javamoney.moneta.Money;

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
public class ShoppingCartEntry implements Serializable {

	private static final long serialVersionUID = 1L;

	private final Article article;
	@Setter
	private int position;
	@Setter
	private int count;
	@Setter
	private Money total;
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

	public void assignVoucher(final Voucher voucher) {
		this.basket.assignVoucher(voucher);
	}

	public void clearVouchers() {
		this.basket.clear();
		this.subEntries.stream().forEach(ShoppingCartSubEntry::clearVouchers);
	}

	public void splitAll() {
		this.subEntries.clear();
		this.subEntries.add(new ShoppingCartSubEntry(this, this.count));
	}

	public String toString() {
		final StringBuilder sb = new StringBuilder();
		return this.toString(sb, 0).toString();
	}

	public StringBuilder toString(final StringBuilder sb, final int indent) {
		this.identation(sb, indent)
				.append(this.getClass().getName()).append("\n");
		this.identation(sb, indent + 1)
				.append("article: ").append(this.article.toString()).append("\n");
		this.identation(sb, indent + 1)
				.append("count: ").append(this.count)
				.append(", total: ").append(this.total).append("\n");
		this.identation(sb, indent + 1)
				.append("entry basket:\n");
		this.basket.toString(sb, indent + 2);
		this.identation(sb, indent + 1)
				.append("subentries:\n");
		this.subEntries.stream().forEach(e -> e.toString(sb, indent + 2));
		return sb;
	}

	private StringBuilder identation(final StringBuilder sb, final int tabs) {
		for (int i = 0; i < tabs; i++) {
			sb.append('\t');
		}
		return sb;
	}
}
