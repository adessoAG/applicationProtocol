package de.adesso.example.application;

import java.math.BigDecimal;

import javax.money.CurrencyUnit;
import javax.money.Monetary;

public class Standard {

	public static final CurrencyUnit EUROS = Monetary.getCurrency("EUR");
	public static final BigDecimal employeeDiscount = BigDecimal.valueOf(20.0);

	/**
	 * The class provides standard constant values, therefore instantiation in not
	 * useful.
	 */
	private Standard() {
	}
}
