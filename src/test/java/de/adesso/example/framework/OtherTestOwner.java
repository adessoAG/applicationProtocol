package de.adesso.example.framework;

import java.util.UUID;

public class OtherTestOwner extends ApplicationOwner {

	final static UUID ownerId = UUID.randomUUID();

	@Override
	protected UUID getOwnerId() {
		return ownerId;
	}
}
