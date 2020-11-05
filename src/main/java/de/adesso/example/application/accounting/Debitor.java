package de.adesso.example.application.accounting;

import java.util.UUID;

import org.javamoney.moneta.Money;

/**
 * Accounts we sell to.
 *
 * @author Matthias
 *
 */
public class Debitor extends Account {

	public Debitor(final UUID id) {
		super(id, Money.of(0.0, de.adesso.example.application.Standard.EUROS));
	}

	@Override
	public String toString() {
		return "debitor (" + this.getId().toString() + "): " + this.getAmount().toString();
	}

}
