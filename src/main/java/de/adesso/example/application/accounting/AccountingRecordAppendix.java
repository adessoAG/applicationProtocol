package de.adesso.example.application.accounting;

import java.util.UUID;

import org.javamoney.moneta.Money;

import de.adesso.example.framework.ApplicationAppendix;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AccountingRecordAppendix extends ApplicationAppendix {
	private static UUID accountingRecordUUID = UUID.randomUUID();

	private Account debitor;
	private Account creditor;
	private Money value;

	public AccountingRecordAppendix(Account debitor, Account creditor, Money value) {
		super(accountingRecordUUID, Accounting.getId());
		this.debitor = debitor;
		this.creditor = creditor;
		this.value = value;
	}
}
