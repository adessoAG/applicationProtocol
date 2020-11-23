package de.adesso.example.application.accounting;

import java.util.UUID;

import org.javamoney.moneta.Money;

import de.adesso.example.application.Standard;

/**
 * Account we buy from.
 *
 * @author Matthias
 *
 */
public class Creditor extends Account {

	private static final long serialVersionUID = -4753997495226916881L;

	public Creditor(final UUID id) {
		super(id, Money.of(0.0, Standard.EUROS));
	}

	@Override
	public String toString() {
		return "creditor (" + this.getId().toString() + "): " + this.getAmount().toString();
	}
}
