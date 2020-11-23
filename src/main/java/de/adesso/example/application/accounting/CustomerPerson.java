package de.adesso.example.application.accounting;

import java.util.UUID;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@EqualsAndHashCode(callSuper = true)
public class CustomerPerson extends Customer {

	private static final long serialVersionUID = -4227031185072142590L;

	CustomerPerson(final UUID customerId, final String firstName, final String name) {
		super(customerId);
		this.firstName = firstName;
		this.name = name;
	}

	private final String firstName;
	private final String name;
}
