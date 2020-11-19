package de.adesso.example.application;

import org.javamoney.moneta.Money;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;

import de.adesso.example.application.employment.EmployeeAppendix;
import de.adesso.example.application.employment.Employment;
import de.adesso.example.application.marketing.Marketing;
import de.adesso.example.application.marketing.Voucher;
import de.adesso.example.application.marketing.VoucherAppendix;
import de.adesso.example.application.shopping.ShoppingBean;
import de.adesso.example.application.shopping.ShoppingCart;
import de.adesso.example.application.stock.Article;
import de.adesso.example.framework.ApplicationProtocol;

@Service
public class ClientExample implements CommandLineRunner {

	private final PriceCalculator manualPriceCalculator;
	private final ShoppingBean shoppingBean;

	private final Cashier cashier;

	private final Employment employment;
	private final Marketing marketing;

	@Autowired
	public ClientExample(
			final PriceCalculator priceCalculator,
			final ShoppingBean shoppingBean,
			final Cashier cashier,
			final Employment employment,
			final Marketing marketing) {
		this.manualPriceCalculator = priceCalculator;
		this.shoppingBean = shoppingBean;
		this.cashier = cashier;
		this.employment = employment;
		this.marketing = marketing;
	}

	@Override
	public void run(final String... args) throws Exception {

		// customer informs about the price
		System.out.println("calculate the price of a simple product with help of the manual configured calculator");
		Article article = this.customerEnteredArticle();
		ApplicationProtocol<Money> state = new ApplicationProtocol<>();
		state = this.manualPriceCalculator.calculatePrice(article, state);
		Money price = state.getResult();
		System.out.println(String.format("%s: %s", article.getArticelId(), price));
		System.out.println("The protocol is: ");
		System.out.println(state.toString());

		// customer informs about the price
		System.out
				.println("calculate the price of a simple product with help of the by annotation generated caclulator");
		article = this.customerEnteredArticle();
		state = new ApplicationProtocol<>();
		state = this.shoppingBean.calculatePriceOfArticle(article, state);
		final Money priceAnnotated = state.getResult();
		System.out.println(String.format("based on annotation: %s: %s", article.getArticelId(), priceAnnotated));
		System.out.println("The protocol is: ");
		System.out.println(state.toString());

		// employee informs about the price
		System.out.println("calculate the price of a simple product for an employee");
		article = this.customerEnteredArticle();
		state = new ApplicationProtocol<>();
		state.addAppendix(new EmployeeAppendix(this.employment.createEmployee("Müller", "Hans", 1234)));
		state = this.shoppingBean.calculatePriceOfArticle(article, state);
		price = state.getResult();
		System.out.println(String.format("%s: %s", article.getArticelId(), price));
		System.out.println("The protocol is: ");
		System.out.println(state.toString());

		// discount with use of a voucher
		System.out.println("calculate the price while using a voucher");
		article = this.customerEnteredArticle();
		state = new ApplicationProtocol<>();
		Voucher tenEuroDiscount = this.marketing.createTenEuroDiscount();
		state.addAppendix(new VoucherAppendix(tenEuroDiscount));
		state = this.shoppingBean.calculatePriceOfArticle(article, state);
		price = state.getResult();
		System.out.println(String.format("%s: %s", article.getArticelId(), price));
		System.out.println("The protocol is: ");
		System.out.println(state.toString());

		// discount with use of a voucher for an employee
		System.out.println("calculate the price while using a voucher for an employee");
		article = this.customerEnteredArticle();
		state = new ApplicationProtocol<>();
		tenEuroDiscount = this.marketing.createTenEuroDiscount();
		state.addAppendix(new VoucherAppendix(tenEuroDiscount));
		state.addAppendix(new EmployeeAppendix(this.employment.createEmployee("Müller", "Hans", 1234)));
		state = this.shoppingBean.calculatePriceOfArticle(article, state);
		price = state.getResult();
		System.out.println(String.format("%s: %s", article.getArticelId(), price));
		System.out.println("The protocol is: ");
		System.out.println(state.toString());

		// book the last calculation
		final ShoppingCart cart = this.buildShoppingCart();
		this.cashier.encash(cart, state);
	}

	private ShoppingCart buildShoppingCart() {
		final ShoppingCart cart = new ShoppingCart();
		cart.addEntry(new Article("334455"));
		return cart;
	}

	private Article customerEnteredArticle() {
		return new Article("112244");
	}

}
