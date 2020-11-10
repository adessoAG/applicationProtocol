package de.adesso.example.application.accounting;

import java.util.UUID;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class CustomerPerson extends Customer {

	CustomerPerson(final UUID customerId, final String firstName, final String name) {
		super(customerId);
		this.firstName = firstName;
		this.name = name;
	}

	private final String firstName;
	private final String name;
}
