package de.adesso.example.application.shopping;

import org.javamoney.moneta.Money;

import de.adesso.example.framework.ApplicationProtocol;
import de.adesso.example.framework.annotation.CallStrategy;
import de.adesso.example.framework.annotation.CallingStrategy;
import de.adesso.example.framework.annotation.Required;
import de.adesso.example.framework.core.ParallelSplit;

public class ShoppingCartSplit extends ParallelSplit {

	@CallStrategy(strategy = CallingStrategy.EAGER)
	public ApplicationProtocol<Money> splitArticles(
			@Required final ShoppingCart cart,
			@Required final ApplicationProtocol<ShoppingCart> state) {
		return null;
	}
}
