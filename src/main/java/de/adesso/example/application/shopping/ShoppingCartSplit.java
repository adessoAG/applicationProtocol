package de.adesso.example.application.shopping;

import java.util.concurrent.Future;

import org.javamoney.moneta.Money;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;

import de.adesso.example.application.PriceCalculatorAnnotated;
import de.adesso.example.application.accounting.Customer;
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
		appendixes.addAppendix(null, customer);
		appendixes.addAllAppendixes(null, subEntry.getAllVouchers());

		final ApplicationProtocol<Money> result = queue.calculatePriceOfArticle(
				subEntry.getEntry().getArticle(),
				appendixes);
		result.setResult(result.getResult().multiply(subEntry.getCount()));
		subEntry.setTotal(result.getResult());
		final AsyncResult<ApplicationProtocol<Money>> future = new AsyncResult<>(result);

		return future;
	}
}
