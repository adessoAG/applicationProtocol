package de.adesso.example.framework;

import java.util.UUID;

public class TestOwner extends ApplicationOwner {

	final static UUID ownerId = UUID.randomUUID();

	@Override
	protected UUID getOwner() {
		return ownerId;
	}
}
