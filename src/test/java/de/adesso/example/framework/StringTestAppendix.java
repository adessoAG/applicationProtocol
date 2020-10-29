package de.adesso.example.framework;

import java.util.UUID;

import de.adesso.example.framework.annotation.Appendix;

@Appendix
public class StringTestAppendix extends ApplicationAppendix<String> {

	public StringTestAppendix(final String content) {
		super(content);
	}

	@Override
	public UUID getOwner() {
		return TestOwner.ownerId;
	}

}
