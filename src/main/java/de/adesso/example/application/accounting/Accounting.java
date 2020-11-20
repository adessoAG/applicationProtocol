package de.adesso.example.application.accounting;

import java.util.UUID;

import de.adesso.example.framework.ApplicationOwner;

public class Accounting extends ApplicationOwner {

	static final UUID id = UUID.randomUUID();
	static final CustomerPerson unknownCustomer = new CustomerPerson(UUID.randomUUID(), "unknown", "customer");
	static final Creditor revenueAccount = new Creditor(UUID.randomUUID());

	@Override
	protected UUID getOwnerId() {
		return id;
	}

	public static CustomerPerson getUnknownCustomer() {
		return unknownCustomer;
	}

	public static Creditor getRevenueAccount() {
		return revenueAccount;
	}
}
