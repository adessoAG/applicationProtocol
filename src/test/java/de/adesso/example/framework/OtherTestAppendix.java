package de.adesso.example.framework;

import java.util.UUID;

import de.adesso.example.framework.annotation.Appendix;

@Appendix
public class OtherTestAppendix extends ApplicationAppendix<Other> {

	protected OtherTestAppendix(final Other content) {
		super(content);
	}

	@Override
	public UUID getOwner() {
		return OtherTestOwner.ownerId;
	}

}
