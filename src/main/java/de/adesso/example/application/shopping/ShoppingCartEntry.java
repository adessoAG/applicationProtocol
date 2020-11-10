package de.adesso.example.application.shopping;

import de.adesso.example.application.stock.Article;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

/**
 * The class describes an entry in the shopping cart. It is the customer request
 * to buy an article count times.
 * <p>
 * The entry has to position information. position is the position within the
 * cart. If discounts are applied, which are only applicable to a single
 * product, and if the entry contains more than one article, the entry needs to
 * be split.
 *
 * @author Matthias
 *
 */
@Getter
@EqualsAndHashCode
public class ShoppingCartEntry {

	private final Article article;
	@Setter
	private int position;
	@Setter
	private int subPosition;
	@Setter
	private int count;

	public ShoppingCartEntry(final Article article, final int count) {
		this.article = article;
		this.count = count;
	}

	public void add(final int number) {
		this.count += number;
	}
}
