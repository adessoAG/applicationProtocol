package de.adesso.example.application.stock;

import java.util.UUID;

import de.adesso.example.framework.ApplicationOwner;

public class Warehousing extends ApplicationOwner {

	final static UUID ownerId = UUID.randomUUID();

	@Override
	protected UUID getOwnerId() {
		return ownerId;
	}
}
