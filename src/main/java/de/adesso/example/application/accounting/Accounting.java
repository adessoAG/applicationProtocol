package de.adesso.example.application.accounting;

import java.util.UUID;

import de.adesso.example.framework.ApplicationOwner;

public class Accounting extends ApplicationOwner {

	final static UUID id = UUID.randomUUID();
	final static CustomerPerson unknownCustomer = new CustomerPerson(UUID.randomUUID(), "unknown", "customer");
	final static Creditor revenueAccount = new Creditor(UUID.randomUUID());

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
