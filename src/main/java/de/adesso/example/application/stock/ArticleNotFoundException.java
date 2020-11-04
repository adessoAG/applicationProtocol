package de.adesso.example.application.stock;

public class ArticleNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 9041564769406751187L;

	public ArticleNotFoundException(final String articelId) {
		super(String.format("article %i not found", articelId));
	}

}
