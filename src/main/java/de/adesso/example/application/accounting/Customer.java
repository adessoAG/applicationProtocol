package de.adesso.example.application.accounting;

import java.util.UUID;

/**
 * The customer is a special kind of debtor.
 *
 * @author Matthias
 *
 */
public class Customer extends Debtor {

	private static final long serialVersionUID = 26384465296502720L;

	public Customer(final UUID id) {
		super(id);
	}

}
