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
package de.adesso.example.application.marketing;

import java.util.HashSet;
import java.util.Set;

import lombok.Getter;

/**
 * Each type which can carry an voucher should have an attribute of type
 * VoucherBasket. It ensures, that only compatible vouchers are assigned.
 *
 * @author Matthias
 *
 */
@Getter
public class VoucherBasket {

	private final VoucherApplication level;
	private final Set<Voucher> vouchers = new HashSet<>();

	public VoucherBasket(final VoucherApplication level) {
		this.level = level;
	}

	/**
	 * Assign an voucher to the basket while checking all constraints of voucher
	 * assignment.
	 *
	 * @param voucher the voucher to be added to the basket
	 */
	public void assignVoucher(final Voucher voucher) {
		if (!this.isAssignable(voucher)) {
			// voucher not applicable on this level
			throw VoucherNotUtilizableException.wrongLevelException(voucher, this.level);
		}

		// all is fine
		this.vouchers.add(voucher);
		voucher.tryUtilize(); // final utilization during booking operation
	}

	/**
	 * Check if the voucher can be assigned to this basket.
	 *
	 * @param voucher the voucher to be tested
	 */
	public boolean isAssignable(final Voucher voucher) {
		if (!voucher.isTryUtilizable()) {
			return false;
		}

		if (!voucher.getApplicableAt().contains(this.level)) {
			return false;
		}

		// all vouchers within the basket are compatible to the new one?
		return this.vouchers.stream()
				.allMatch(v -> v.isCompatible(voucher));
	}

	/**
	 * Remove a voucher from the basket.
	 *
	 * @param voucher the voucher to be removed
	 */
	public void removerVoucher(final Voucher voucher) {
		this.vouchers.remove(voucher);
	}

	public boolean containsVoucherOfType(final VoucherType type) {
		return !this.vouchers.stream()
				.filter(v -> v.getType() == type)
				.findFirst().isEmpty();
	}

	/**
	 * Through out all vouchers of the basket
	 */
	public void clear() {
		this.vouchers.clear();
	}

	public String toString() {
		final StringBuilder sb = new StringBuilder();
		return this.toString(sb, 0).toString();
	}

	public StringBuilder toString(final StringBuilder sb, final int indent) {
		this.identation(sb, indent)
				.append(this.getClass().getName()).append("\n");
		this.identation(sb, indent + 1)
				.append("level: ").append(this.level).append("\n");
		this.identation(sb, indent + 1)
				.append("count: ").append(this.vouchers.size()).append("\n");
		this.vouchers.stream().forEach(voucher -> voucher.toString(sb, indent + 2));
		return sb;
	}

	private StringBuilder identation(final StringBuilder sb, final int tabs) {
		for (int i = 0; i < tabs; i++) {
			sb.append('\t');
		}
		return sb;
	}
}
