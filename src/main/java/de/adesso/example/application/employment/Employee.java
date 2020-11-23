package de.adesso.example.application.employment;

import java.io.Serializable;
import java.util.UUID;

import org.javamoney.moneta.Money;

import de.adesso.example.application.Standard;
import de.adesso.example.application.accounting.Customer;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@ToString
public class Employee implements Serializable {

	private static final long serialVersionUID = 800784862436922847L;

	private final String name;
	private final String firstName;
	private final int id;
	private final Customer employeeCustomer = new Customer(UUID.randomUUID());

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
