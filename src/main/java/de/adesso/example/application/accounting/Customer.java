package de.adesso.example.application.accounting;

import java.util.UUID;

/**
 * The customer is a special kind of debitor.
 *
 * @author Matthias
 *
 */
public class Customer extends Debitor {

	public Customer(final UUID id) {
		super(id);
	}

}
