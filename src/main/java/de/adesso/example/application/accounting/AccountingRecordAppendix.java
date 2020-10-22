package de.adesso.example.application.accounting;

import java.util.UUID;

import org.javamoney.moneta.Money;

import de.adesso.example.framework.ApplicationAppendix;
import de.adesso.example.framework.annotation.Appendix;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@Appendix()
public class AccountingRecordAppendix extends ApplicationAppendix {

	private static UUID accountingRecordUUID = UUID.randomUUID();

	private final Account debitor;
	private final Account creditor;
	private final Money value;

	public AccountingRecordAppendix(final Account debitor, final Account creditor, final Money value) {
		super(accountingRecordUUID, Accounting.getId());
		this.debitor = debitor;
		this.creditor = creditor;
		this.value = value;
	}
}
