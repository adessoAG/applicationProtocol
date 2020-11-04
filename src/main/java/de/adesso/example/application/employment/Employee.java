package de.adesso.example.application.employment;

import org.javamoney.moneta.Money;

import de.adesso.example.application.Standard;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@ToString
public class Employee {

	private final String name;
	private final String firstName;
	private final int id;

	@Setter(value = AccessLevel.PACKAGE)
	private Money income;
	private final Money benefit = Money.of(0.0, Standard.EUROS);

	Employee(final String firstName, final String name, final int id) {
		this.firstName = firstName;
		this.name = name;
		this.id = id;
	}

	void registerBenefit(final EmployeeBenefit benefitRecord) {
		this.benefit.add(benefitRecord.getBenefit());
	}
}
