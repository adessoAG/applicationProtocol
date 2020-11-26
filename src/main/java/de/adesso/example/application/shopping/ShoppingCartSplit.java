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

import java.util.concurrent.Future;
import java.util.stream.Collectors;

import org.javamoney.moneta.Money;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;

import de.adesso.example.application.PriceCalculatorAnnotated;
import de.adesso.example.application.accounting.Customer;
import de.adesso.example.application.accounting.CustomerAppendix;
import de.adesso.example.application.marketing.VoucherAppendix;
import de.adesso.example.framework.ApplicationProtocol;
import de.adesso.example.framework.core.ParallelSplit;

@Service
public class ShoppingCartSplit extends ParallelSplit {

	@Async
	public Future<ApplicationProtocol<Money>> execute(
			final PriceCalculatorAnnotated queue,
			final ShoppingCartSubEntry subEntry,
			final Customer customer) {

		final ApplicationProtocol<Money> appendixes = new ApplicationProtocol<>();
		appendixes.addAppendix(new CustomerAppendix(customer));
		appendixes.addAllAppendixes(subEntry.getAllVouchers().stream()
				.map(VoucherAppendix::new).collect(Collectors.toSet()));

		final ApplicationProtocol<Money> result = queue.calculatePriceOfArticle(
				subEntry.getEntry().getArticle(),
				appendixes);
		result.setResult(result.getResult().multiply(subEntry.getCount()));
		subEntry.setTotal(result.getResult());
		final AsyncResult<ApplicationProtocol<Money>> future = new AsyncResult<>(result);

		return future;
	}
}
