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

import java.util.UUID;

import org.javamoney.moneta.Money;
import org.springframework.stereotype.Service;

import de.adesso.example.application.Standard;
import de.adesso.example.application.accounting.Creditor;
import de.adesso.example.framework.ApplicationOwner;

@Service
public class Marketing extends ApplicationOwner {

	private static final UUID marketingOwner = UUID.randomUUID();

	private static final Creditor marketingVoucherAccount = new Creditor(UUID.randomUUID());

	@Override
	protected UUID getOwnerId() {
		return marketingOwner;
	}

	public static Creditor getMarketingVoucherAccount() {
		return marketingVoucherAccount;
	}

	public static UUID getMarketingOwner() {
		return marketingOwner;
	}

	public Voucher createTenEuroDiscount() {
		return new VoucherDiscountAbsolute("10EuroDiscount", Money.of(10.00, Standard.EUROS));
	}
}
