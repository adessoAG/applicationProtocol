package de.adesso.example.application.accounting;

import java.util.Collection;

public class BookKeeper {

	public void process (final Collection<AccountingRecordAppendix> accountingRecords) throws BookKeepingException {
		accountingRecords
		.stream()
		.forEach (this::book);
	}

	public void book(final AccountingRecordAppendix ar) {
		ar.getCreditor().amount.subtract(ar.getValue());
		ar.getDebitor().amount.add(ar.getValue());
	}
}
