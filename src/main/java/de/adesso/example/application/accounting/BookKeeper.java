package de.adesso.example.application.accounting;

import java.util.Collection;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Transactional(propagation = Propagation.REQUIRED)
public class BookKeeper {
	
	public void process (Collection<AccountingRecordAppendix> accountingRecords) throws BookKeepingException {
		accountingRecords
			.stream()
			.forEach (ar -> book(ar));
	}

	public void book(AccountingRecordAppendix ar) {
		ar.getCreditor().amount.subtract(ar.getValue());
		ar.getDebitor().amount.add(ar.getValue());
	}
}
