package de.adesso.example.application.stock;

import java.util.UUID;

import de.adesso.example.framework.ApplicationAppendix;

public class ArticleAppendix extends ApplicationAppendix<Article> {

	protected ArticleAppendix(final Article content) {
		super(content);
	}

	@Override
	public UUID getOwner() {
		return Warehousing.ownerId;
	}
}
