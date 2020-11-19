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
import de.adesso.example.application.accounting.AccountingRecordAppendix;
import de.adesso.example.application.accounting.Customer;
import de.adesso.example.application.marketing.Voucher;
import de.adesso.example.framework.ApplicationProtocol;
import de.adesso.example.framework.annotation.CallStrategy;
import de.adesso.example.framework.annotation.CallingStrategy;
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

	@CallStrategy(strategy = CallingStrategy.EAGER)
	public ApplicationProtocol<ShoppingCart> priceCartParallel(
			final ShoppingCart cart,
			final Customer customer,
			final Set<Voucher> vouchers,
			final ApplicationProtocol<ShoppingCart> state) {

		this.clearState(state, cart);
		final List<Future<ApplicationProtocol<Money>>> result = cart.getAllEntries().stream()
				.map(ShoppingCartEntry::getSubEntries)
				.flatMap(List::stream)
				.map(se -> this.splitter.execute(this.queue, se, customer, vouchers))
				.collect(Collectors.toList());

		return this.joinResult(result, cart, state);
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
		state.removeAll(AccountingRecordAppendix.class);
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
		state.addAllAppendixes(singleResult.getAllAppenixesOfTypeAsList(AccountingRecordAppendix.class));
		return null;
	}
}
