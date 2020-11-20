package de.adesso.example.application.accounting;

import java.util.UUID;

/**
 * The customer is a special kind of debtor.
 *
 * @author Matthias
 *
 */
public class Customer extends Debtor {

	public Customer(final UUID id) {
		super(id);
	}

}
