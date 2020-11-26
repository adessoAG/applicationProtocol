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

import java.util.List;
import java.util.Set;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

import org.javamoney.moneta.Money;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import de.adesso.example.application.PriceCalculatorAnnotated;
import de.adesso.example.application.Standard;
import de.adesso.example.application.accounting.AccountingRecord;
import de.adesso.example.application.accounting.Customer;
import de.adesso.example.application.marketing.Voucher;
import de.adesso.example.framework.ApplicationProtocol;
import de.adesso.example.framework.annotation.CallStrategy;
import de.adesso.example.framework.annotation.CallingStrategy;
import de.adesso.example.framework.annotation.Required;
import de.adesso.example.framework.core.ParallelJoin;
import lombok.SneakyThrows;

@Service
public class ShoppingCartCalculator extends ParallelJoin {

	private final PriceCalculatorAnnotated queue;
	private final ShoppingCartSplit splitter;

	@Autowired
	public ShoppingCartCalculator(final PriceCalculatorAnnotated queue, final ShoppingCartSplit splitter) {
		this.splitter = splitter;
		this.queue = queue;
	}

	/**
	 * This is the parallel running part of the calculation. It splits the cart into
	 * the various products and calculates them in parallel.
	 *
	 * @param cart     the cart to be calculated
	 * @param customer the customer of the cart
	 * @param vouchers the vouchers the customer provided
	 * @param state    state of the calculation
	 * @return the enriched state
	 */
	@CallStrategy(strategy = CallingStrategy.EAGER)
	public ApplicationProtocol<ShoppingCart> priceCartParallel(
			@Required final ShoppingCart cart,
			@Required final Customer customer,
			@Required final Set<Voucher> vouchers,
			@Required final ApplicationProtocol<ShoppingCart> state) {

		this.clearState(state, cart);
		final List<Future<ApplicationProtocol<Money>>> result = cart.getAllEntries().stream()
				.map(ShoppingCartEntry::getSubEntries)
				.flatMap(List::stream)
				.map(se -> this.splitter.execute(this.queue, se, customer))
				.collect(Collectors.toList());

		return this.joinResult(result, cart, state);
	}

	/**
	 * Initialize calculation of the shopping cart. This is part of the calculation
	 * chain and ensures, that the following steps can relay on proper settings.
	 *
	 * @param cart  the provided shopping cart
	 * @param state the state of the calculation
	 * @return the state to be provided to the next step
	 */
	@CallStrategy(strategy = CallingStrategy.EAGER)
	public ApplicationProtocol<ShoppingCart> initCartProcessing(
			@Required final ShoppingCart cart,
			@Required final ApplicationProtocol<ShoppingCart> state) {
		state.setResult(cart);
		return state;
	}

	/**
	 * This method will get on the futures, thus it waits till the result of the
	 * calculation is present.
	 *
	 * @param futures parallel calculated results
	 * @param cart    the cart being calculated
	 * @param state   the processing state
	 * @return the updated state containing all results
	 */
	private ApplicationProtocol<ShoppingCart> joinResult(
			final List<Future<ApplicationProtocol<Money>>> futures,
			final ShoppingCart cart,
			final ApplicationProtocol<ShoppingCart> state) {

		futures.stream()
				.map(this::consume)
				.forEach(s -> this.combine(s, state, cart));
		cart.getAllEntries().forEach(this::sumSubEntries);
		state.setResult(cart);
		return state;
	}

	/**
	 * Consume the future. The process will block in this function till the
	 * calculation is done.
	 *
	 * @param future the result of a sub calculation
	 * @return the result of the sub calculation
	 */
	@SneakyThrows
	private ApplicationProtocol<Money> consume(final Future<ApplicationProtocol<Money>> future) {
		return future.get();
	}

	/**
	 * Remove outdated results from the state.
	 *
	 * @param state the current state of the processing
	 */
	private void clearState(final ApplicationProtocol<ShoppingCart> state, final ShoppingCart cart) {
		state.removeAll(null, AccountingRecord.class);
		cart.setTotal(Standard.zeroEuros);
	}

	/**
	 * Combine the results of each sub-entry to the result of the cart
	 *
	 * @param singleResult the result of a single sub calculation
	 * @param state
	 * @return
	 */
	private ApplicationProtocol<ShoppingCart> combine(
			final ApplicationProtocol<Money> singleResult,
			final ApplicationProtocol<ShoppingCart> state,
			final ShoppingCart cart) {
		cart.setTotal(cart.getTotal().add(singleResult.getResult()));
		state.transfertAppendixes(singleResult);
		return state;
	}

	private void sumSubEntries(final ShoppingCartEntry entry) {
		final Money total = entry.getSubEntries().stream()
				.map(ShoppingCartSubEntry::getTotal)
				.reduce(Standard.zeroEuros, (a, b) -> a.add(b));
		entry.setTotal(total);
	}
}
