package de.adesso.example.application.accounting;

import org.javamoney.moneta.Money;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AccountingRecord {

	private final Account debitor;
	private final Account creditor;
	private final Money value;
}
