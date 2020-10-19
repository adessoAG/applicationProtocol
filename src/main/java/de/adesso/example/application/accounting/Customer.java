package de.adesso.example.application.accounting;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Customer {

	private final String firstName;
	private final String name;
	private final UUID customerId;
}
