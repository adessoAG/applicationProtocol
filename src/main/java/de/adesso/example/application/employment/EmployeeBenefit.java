package de.adesso.example.application.employment;

import org.javamoney.moneta.Money;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@AllArgsConstructor
@ToString
public class EmployeeBenefit {

	private final Employee employee;
	private final Money benefit;
}
