package de.adesso.example.application.stock;

import java.util.UUID;

import de.adesso.example.framework.ApplicationAppendix;
import de.adesso.example.framework.annotation.Appendix;

@Appendix
public class ArticleAppendix extends ApplicationAppendix<Article> {

	protected ArticleAppendix(final Article content) {
		super(content);
	}

	@Override
	public UUID getOwner() {
		return Warehousing.ownerId;
	}
}
