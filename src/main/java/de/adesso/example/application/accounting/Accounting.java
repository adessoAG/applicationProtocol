package de.adesso.example.application.accounting;

import java.util.UUID;

public class Accounting {
	static private UUID id = UUID.randomUUID();

	public static UUID getId () {
		return id;
	}
}
