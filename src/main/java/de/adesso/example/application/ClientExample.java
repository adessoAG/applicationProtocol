package de.adesso.example.application;

import org.javamoney.moneta.Money;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;

import de.adesso.example.application.employment.EmployeeAppendix;
import de.adesso.example.application.employment.Employment;
import de.adesso.example.application.stock.Article;
import de.adesso.example.framework.ApplicationProtocol;

@Service
public class ClientExample implements CommandLineRunner {

	private final PriceCalculatorAnnotated priceCalculator;

	private final Cashier cashier;

	private final Employment employment;

	@Autowired
	public ClientExample(final PriceCalculatorAnnotated priceCalculator, final Cashier cashier,
			final Employment employment) {
		this.priceCalculator = priceCalculator;
		this.cashier = cashier;
		this.employment = employment;
	}

	@Override
	public void run(final String... args) throws Exception {

		// customer informs about the price
		Article article = this.customerEnteredArticle();
		ApplicationProtocol<Money> state = new ApplicationProtocol<>();
		state = this.priceCalculator.calculatePrice(article, state);
		Money price = state.getResult();
		System.out.println(String.format("%s: %s", article.getArticelId(), price));
		System.out.println("The protocol is: ");
		System.out.println(state.toString());

		// employee informs about the price
		article = this.customerEnteredArticle();
		state = new ApplicationProtocol<>();
		state.addAppendix(new EmployeeAppendix(this.employment.createEmployee("Müller", "Hans", 1234)));
		state = this.priceCalculator.calculatePrice(article, state);
		price = state.getResult();
		System.out.println(String.format("%s: %s", article.getArticelId(), price));
		System.out.println("The protocol is: ");
		System.out.println(state.toString());
	}

	private Article customerEnteredArticle() {
		return new Article("112244");
	}

}
