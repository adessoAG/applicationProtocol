package de.adesso.example.application.accounting;

import java.util.UUID;

import org.javamoney.moneta.Money;

import lombok.ToString;

/**
 * Accounts we sell to.
 *
 * @author Matthias
 *
 */
@ToString
public class Debitor extends Account {

	public Debitor(final UUID id) {
		super(id, Money.of(0.0, de.adesso.example.application.Standard.EUROS));
	}

	@Override
	public String toString() {
		return "debitor (" + this.getId().toString() + "): " + this.getAmount().toString();
	}

}
