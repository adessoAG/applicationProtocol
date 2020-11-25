package de.adesso.example.application.accounting;

import java.util.Collection;

import org.springframework.stereotype.Service;

@Service
public class BookKeeper {

	public void process(final Collection<AccountingRecord> accountingRecords) {
		accountingRecords
				.stream()
				.forEach(this::book);
	}

	public void book(final AccountingRecord ar) {
		ar.getCreditor().amount.subtract(ar.getValue());
		ar.getDebitor().amount.add(ar.getValue());
	}
}
