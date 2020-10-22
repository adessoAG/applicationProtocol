package de.adesso.example.application.accounting;

import java.util.UUID;

import de.adesso.example.framework.ApplicationOwner;

public class Accounting extends ApplicationOwner {

	final static UUID id = UUID.randomUUID();

	@Override
	protected UUID getOwner() {
		return id;
	}
}
