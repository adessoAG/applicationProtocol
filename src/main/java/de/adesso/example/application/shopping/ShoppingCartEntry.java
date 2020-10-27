package de.adesso.example.application.shopping;

import de.adesso.example.application.stock.Article;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

/**
 * The class describes an entry in the shopping cart. It is the customer request
 * to buy an article count times.
 *
 * @author Matthias
 *
 */
@Getter
@AllArgsConstructor
@EqualsAndHashCode
public class ShoppingCartEntry {

	private final Article article;
	private int count;

	public void add(final int number) {
		this.count += number;
	}
}
