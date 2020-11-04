package de.adesso.example.application.employment;

import java.util.UUID;

import de.adesso.example.framework.ApplicationAppendix;

public class EmployeeBenefitAppendix extends ApplicationAppendix<EmployeeBenefit> {

	protected EmployeeBenefitAppendix(final EmployeeBenefit content) {
		super(content);
	}

	@Override
	public UUID getOwner() {
		return Employment.ownUuid;
	}

}
