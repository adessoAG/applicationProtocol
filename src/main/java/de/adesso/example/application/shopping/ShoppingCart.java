package de.adesso.example.application.shopping;

import java.util.List;
import java.util.OptionalInt;
import java.util.stream.IntStream;

import org.springframework.util.Assert;

import de.adesso.example.application.stock.Article;

public class ShoppingCart {

	private List<ShoppingCartEntry> entries;

	public void removePosition(final int pos) {
		Assert.isTrue(this.entries.size() >= pos, "position not available within shopping cart");

		this.entries.remove(pos);
	}

	public void addEntry(final Article article) {
		final OptionalInt pos = posOfArticle(article);
		if (pos.isEmpty()) {
			this.entries.add(new ShoppingCartEntry(article, 1));
		} else {
			this.entries.get(pos.getAsInt()).add(1);
		}
	}

	public void addEntry(final Article article, final int count) {
		final OptionalInt pos = posOfArticle(article);
		if (pos.isEmpty()) {
			this.entries.add(new ShoppingCartEntry(article, count));
		} else {
			this.entries.get(pos.getAsInt()).add(count);
		}
	}

	private OptionalInt posOfArticle(final Article article) {
		return IntStream.range(0, this.entries.size())
				.filter(i -> this.entries.get(i).equals(article))
				.findFirst();
	}
}
