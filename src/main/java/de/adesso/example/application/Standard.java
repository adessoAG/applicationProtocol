package de.adesso.example.application;

import java.math.BigDecimal;

import javax.money.CurrencyUnit;
import javax.money.Monetary;

public interface Standard {

	final CurrencyUnit EUROS = Monetary.getCurrency("EUR");
	final BigDecimal employeeDiscount = BigDecimal.valueOf(20.0);
}
