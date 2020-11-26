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

import lombok.extern.log4j.Log4j2;

@Log4j2
public class VoucherNotUtilizableException extends RuntimeException {

	private static final long serialVersionUID = -7917746111740547420L;

	private VoucherNotUtilizableException(final String message) {
		super(message);
	}

	public static VoucherNotUtilizableException notUtilizable(final Voucher voucher) {
		final String message = "the voucher is not utilizable: " + voucher.getVoucherId();
		log.atInfo().log(message);
		return new VoucherNotUtilizableException(message);
	}

	public static VoucherNotUtilizableException wrongLevelException(final Voucher voucher,
			final VoucherApplication... validApplications) {
		final StringBuilder sb = new StringBuilder()
				.append("voucher: ")
				.append(voucher.getVoucherId())
				.append(": only the VoucherApplications [");
		boolean insertedOne = false;
		for (final VoucherApplication application : validApplications) {
			if (insertedOne) {
				sb.append(", ");
			}
			insertedOne = true;
			sb.append(application);
		}
		sb.append("] are feasable to entries, got: ")
				.append(voucher.getApplicableAt());
		final String message = sb.toString();
		log.atInfo().log(message);
		return new VoucherNotUtilizableException(message);
	}

	public static VoucherNotUtilizableException conflictException(final VoucherApplication level) {
		final String message = "the voucher cannot be used with this article, because there are confliction vouchers";
		log.atInfo().log(message);
		return new VoucherNotUtilizableException(message);
	}
}
