package de.adesso.example.application.shopping;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;

import org.junit.jupiter.api.Test;

import de.adesso.example.application.accounting.Accounting;
import de.adesso.example.application.stock.Article;

class ShoppingCartTest {

	@Test
	void testRemoveEntryArticleOne() {
		// prepare
		final ShoppingCart cart = new ShoppingCart(Accounting.getUnknownCustomer());
		final Article article = new Article("the article id");
		cart.addEntry(article, 1);

		// test
		cart.removeEntry(article);

		// validate
		assertThat(cart.contains(article)).isFalse();
	}

	@Test
	void testRemoveEntryArticleMore() {
		// prepare
		final ShoppingCart cart = new ShoppingCart(Accounting.getUnknownCustomer());
		final Article article = new Article("the article id");
		cart.addEntry(article, 7);

		// test
		cart.removeEntry(article);

		// validate
		assertThat(cart.contains(article)).isFalse();
	}

	@Test
	void testRemoveEntryArticleCountLess() {
		// prepare
		final ShoppingCart cart = new ShoppingCart(Accounting.getUnknownCustomer());
		final Article article = new Article("the article id");
		cart.addEntry(article, 7);

		// test
		cart.removeEntry(article, 5);

		// validate
		assertThat(cart.contains(article)).isTrue();
		final Optional<ShoppingCartEntry> oe = cart.getEntry(article);
		assertThat(oe.get().getCount()).isEqualTo(2);
	}

	@Test
	void testRemoveEntryArticleCountEqual() {
		// prepare
		final ShoppingCart cart = new ShoppingCart(Accounting.getUnknownCustomer());
		final Article article = new Article("the article id");
		cart.addEntry(article, 7);

		// test
		cart.removeEntry(article, 7);

		// validate
		assertThat(cart.contains(article)).isFalse();
	}

	@Test
	void testRemoveEntryArticleCountNotFound() {
		// prepare
		final ShoppingCart cart = new ShoppingCart(Accounting.getUnknownCustomer());
		final Article article = new Article("the article id");
		cart.addEntry(article, 7);

		// test
		cart.removeEntry(new Article("other"), 7);

		// validate
		assertThat(cart.contains(article)).isTrue();
	}

	@Test
	void testAddEntryArticle() {
		// prepare
		final ShoppingCart cart = new ShoppingCart(Accounting.getUnknownCustomer());
		final Article article = new Article("the article id");

		// test
		cart.addEntry(article);

		// validate
		assertThat(cart.contains(article)).isTrue();
		assertThat(cart.getEntry(article).get().getCount()).isEqualTo(1);
	}

	@Test
	void testAddEntryArticleInt() {
		// prepare
		final ShoppingCart cart = new ShoppingCart(Accounting.getUnknownCustomer());
		final Article article = new Article("the article id");

		// test
		cart.addEntry(article, 5);

		// validate
		assertThat(cart.contains(article)).isTrue();
		assertThat(cart.getEntry(article).get().getCount()).isEqualTo(5);
	}

}
