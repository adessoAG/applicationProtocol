package de.adesso.example.framework;

import java.util.UUID;

import de.adesso.example.framework.annotation.Appendix;

@Appendix
public class IntegerTestAppendix extends ApplicationAppendix<Integer> {

	public IntegerTestAppendix(final Integer content) {
		super(content);
	}

	@Override
	public UUID getOwner() {
		return TestOwner.ownerId;
	}

}
