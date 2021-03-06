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
import java.util.Set;

import org.javamoney.moneta.Money;

import de.adesso.example.application.marketing.Voucher;
import de.adesso.example.application.marketing.VoucherApplication;
import de.adesso.example.application.marketing.VoucherBasket;
import de.adesso.example.application.marketing.VoucherNotUtilizableException;
import lombok.Getter;
import lombok.Setter;

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
public class ShoppingCartSubEntry implements Serializable {

	private static final long serialVersionUID = 1L;

	/** vouchers assigned to the sub-entry */
	private final VoucherBasket basket = new VoucherBasket(VoucherApplication.APPLICABLE_TO_SUB_ENTRY);
	/** parent link, each sub entry is part of the corresponding entry */
	private final ShoppingCartEntry entry;
	/** number of entries represented by this sub-entry */
	private final int count;
	/** amount for the sub-entry */
	@Setter
	private Money total;

	public ShoppingCartSubEntry(final ShoppingCartEntry entry, final int count) {
		this.entry = entry;
		this.count = count;
	}

	/**
	 * Get all assigned vouchers.
	 *
	 * @return the assigned vouchers
	 */
	public Set<Voucher> getAllVouchers() {
		return this.basket.getVouchers();
	}

	public void clearVouchers() {
		this.basket.clear();
	}

	/**
	 * Removes the given voucher from the set of vouchers assigned to this
	 * sub-entry. If this voucher is not assigned, does nothing.
	 *
	 * @param voucher the voucher to be removed.
	 */
	public void removeVoucher(final Voucher voucher) {
		this.basket.removerVoucher(voucher);
	}

	/**
	 * Checks if the voucher can be assigned to this sub-entry. Each voucher,
	 * already assigned vouchers and the new voucher, can raise constraints which
	 * hinder assignment.
	 *
	 * @param voucher the voucher to be assigned
	 * @return true if the voucher can be assigned
	 */
	public boolean isAssignable(final Voucher voucher) {
		return this.basket.isAssignable(voucher);
	}

	/**
	 * Assigns the voucher to the sub-entry. Will through the exception
	 * {@link VoucherNotUtilizableException} if the voucher is not assignable.
	 *
	 * @param voucher voucher to be assigned to this sub-entry
	 */
	public void assignVoucher(final Voucher voucher) {
		this.basket.assignVoucher(voucher);
	}

	public String toString() {
		final StringBuilder sb = new StringBuilder();
		return this.toString(sb, 0).toString();
	}

	public StringBuilder toString(final StringBuilder sb, final int indent) {
		this.identation(sb, indent)
				.append(this.getClass().getName()).append("\n");
		this.identation(sb, indent + 1)
				.append("sub-entry basket:\n");
		this.basket.toString(sb, indent + 2);
		this.identation(sb, indent + 1)
				.append("count: ").append(this.count)
				.append(", total: ").append(this.total).append("\n");
		return sb;
	}

	private StringBuilder identation(final StringBuilder sb, final int tabs) {
		for (int i = 0; i < tabs; i++) {
			sb.append('\t');
		}
		return sb;
	}
}
