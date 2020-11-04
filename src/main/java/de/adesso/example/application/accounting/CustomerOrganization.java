package de.adesso.example.application.accounting;

import java.util.UUID;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class CustomerOrganization extends Debitor {

	CustomerOrganization(final UUID customerId, final String name) {
		super(customerId);
		this.name = name;
	}

	private final String name;
}
