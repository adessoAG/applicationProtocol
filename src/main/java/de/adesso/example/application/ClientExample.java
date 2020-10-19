package de.adesso.example.application;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;

import de.adesso.example.application.stock.Article;
import de.adesso.example.framework.ApplicationProtocol;

public class ClientExample implements CommandLineRunner {

	private final PriceCalculator priceCalculator;

	private final Cashier cashier;

	@Autowired
	public ClientExample(final PriceCalculator priceCalculator, final Cashier cashier) {
		this.priceCalculator = priceCalculator;
		this.cashier = cashier;
	}

	@Override
	public void run(final String... args) throws Exception {

		// customer informs about the price
		final Article article = customerEnteredArticle();
		final ApplicationProtocol<BigDecimal> state = this.priceCalculator.calculatePrice(article, null);
		final BigDecimal price = state.getResult();
		System.out.println(String.format("%d: %.2f €", article, price));
		System.out.println("The protocol is: ");
		System.out.println(state.toString());
	}

	private Article customerEnteredArticle() {
		// TODO Auto-generated method stub
		return null;
	}

}
