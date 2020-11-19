package de.adesso.example.application.shopping;

import de.adesso.example.application.stock.Article;
import de.adesso.example.framework.ApplicationProtocol;
import de.adesso.example.framework.annotation.CallStrategy;
import de.adesso.example.framework.annotation.CallingStrategy;
import de.adesso.example.framework.core.ParallelJoin;

public class ShoppingCartJoin extends ParallelJoin {

	@CallStrategy(strategy = CallingStrategy.EAGER)
	public ApplicationProtocol<ShoppingCart> joinArticles(final ApplicationProtocol<Article> state) {
		return null;
	}
}
