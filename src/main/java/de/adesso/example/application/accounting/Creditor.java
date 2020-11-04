package de.adesso.example.application.accounting;

import java.util.UUID;

import org.javamoney.moneta.Money;

import de.adesso.example.application.Standard;
import lombok.ToString;

/**
 * Account we buy from.
 *
 * @author Matthias
 *
 */
@ToString
public class Creditor extends Account {

	public Creditor(final UUID id) {
		super(id, Money.of(0.0, Standard.EUROS));
	}
}
