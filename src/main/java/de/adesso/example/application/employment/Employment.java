package de.adesso.example.application.employment;

import java.util.UUID;

import de.adesso.example.framework.AppendixOwner;

public class Employment extends AppendixOwner {

	public final static UUID ownUuid = UUID.randomUUID();

	public Employment() {
		super(ownUuid);
	}

	public static UUID employeeAppendixId = UUID.randomUUID();

	public EmployeeAppendix createEmployeeAppendix(final Employee employee) {
		return new EmployeeAppendix(employeeAppendixId, getOwnUuid(), employee);
	}
}
