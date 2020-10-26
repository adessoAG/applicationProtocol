package de.adesso.example.framework;

import java.util.UUID;

import de.adesso.example.framework.annotation.Appendix;

@Appendix
public class OtherStringTestAppendix extends ApplicationAppendix<String> {

	protected OtherStringTestAppendix(final String content) {
		super(content);
	}

	@Override
	public UUID getOwner() {
		return OtherTestOwner.ownerId;
	}

}
