package de.adesso.example.application.employment;

import java.util.UUID;

import de.adesso.example.framework.ApplicationAppendix;
import lombok.Getter;

@Getter
public class EmployeeAppendix extends ApplicationAppendix {

	private final Employee employee;

	EmployeeAppendix(final UUID applicationAppendixId, final UUID owner, final Employee employee) {
		super(applicationAppendixId, owner);
		this.employee = employee;
	}
}
