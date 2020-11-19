package de.adesso.example.application.accounting;

import java.util.Collection;

import org.springframework.stereotype.Service;

@Service
public class BookKeeper {

	public void process(final Collection<AccountingRecordAppendix> accountingRecords) {
		accountingRecords
				.stream()
				.forEach(this::book);
	}

	public void book(final AccountingRecordAppendix ara) {
		final AccountingRecord ar = ara.getContent();
		ar.getCreditor().amount.subtract(ar.getValue());
		ar.getDebitor().amount.add(ar.getValue());
	}
}
