package de.adesso.example.application.shopping;

import java.util.Set;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

import org.javamoney.moneta.Money;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;

import de.adesso.example.application.PriceCalculatorAnnotated;
import de.adesso.example.application.accounting.Customer;
import de.adesso.example.application.accounting.CustomerAppendix;
import de.adesso.example.application.marketing.Voucher;
import de.adesso.example.application.marketing.VoucherAppendix;
import de.adesso.example.framework.ApplicationProtocol;
import de.adesso.example.framework.core.ParallelSplit;

@Service
public class ShoppingCartSplit extends ParallelSplit {

	@Async
	public Future<ApplicationProtocol<Money>> execute(
			final PriceCalculatorAnnotated queue,
			final ShoppingCartSubEntry subEntry,
			final Customer customer,
			final Set<Voucher> vouchers) {

		final ApplicationProtocol<Money> appendixes = new ApplicationProtocol<>();
		appendixes.addAppendix(new CustomerAppendix(customer));
		appendixes.addAllAppendixes(vouchers.stream().map(VoucherAppendix::new).collect(Collectors.toSet()));

		final ApplicationProtocol<Money> result = queue.calculatePriceOfArticle(
				subEntry.getEntry().getArticle(),
				appendixes);
		final AsyncResult<ApplicationProtocol<Money>> future = new AsyncResult<>(result);

		return future;
	}
}
