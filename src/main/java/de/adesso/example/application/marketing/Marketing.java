package de.adesso.example.application.marketing;

import java.util.UUID;

import de.adesso.example.framework.ApplicationOwner;

public class Marketing extends ApplicationOwner {

	public static final UUID voucherAppendixId = UUID.randomUUID();
	public static final UUID marketingOwner = UUID.randomUUID();

	@Override
	protected UUID getOwner() {
		// TODO Auto-generated method stub
		return marketingOwner;
	}
}
