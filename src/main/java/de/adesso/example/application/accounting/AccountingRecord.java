package de.adesso.example.application.accounting;

import java.io.Serializable;

import org.javamoney.moneta.Money;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AccountingRecord implements Serializable {

	private static final long serialVersionUID = -901694136354843044L;

	private final Account debitor;
	private final Account creditor;
	private final Money value;

	public String toString() {
		return this.getClass().getName() + "(" + this.debitor.toString() + ", " + this.creditor.toString() + ", "
				+ this.value.toString() + ")";
	}
}
