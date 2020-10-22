package de.adesso.example.application.accounting;

import java.util.UUID;

import de.adesso.example.framework.ApplicationAppendix;
import de.adesso.example.framework.annotation.Appendix;

@Appendix()
public class AccountingRecordAppendix extends ApplicationAppendix<AccountingRecord> {

	private static UUID accountingRecordUUID = UUID.randomUUID();

	public AccountingRecordAppendix(final AccountingRecord accountingRecord) {
		super(accountingRecord);
	}

	@Override
	public UUID getOwner() {
		return Accounting.id;
	}

	@Override
	public UUID getAppendixId() {
		return accountingRecordUUID;
	}
}
