package de.adesso.example.application.stock;

import java.util.UUID;

import de.adesso.example.framework.ApplicationAppendix;

public class ArticleAppendix extends ApplicationAppendix<Article> {

	protected ArticleAppendix(Article content) {
		super(content);
	}

	final static UUID articelAppendixId = UUID.randomUUID();

	@Override
	public UUID getOwner() {
		return Warehousing.ownerId;
	}

	@Override
	public UUID getAppendixId() {
		return articelAppendixId;
	}

}
