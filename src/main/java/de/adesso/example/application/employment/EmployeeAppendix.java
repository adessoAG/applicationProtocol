package de.adesso.example.application.employment;

import java.util.UUID;

import de.adesso.example.framework.ApplicationAppendix;
import de.adesso.example.framework.annotation.Appendix;
import lombok.Getter;

@Getter
@Appendix
public class EmployeeAppendix extends ApplicationAppendix<Employee> {

	EmployeeAppendix(final Employee employee) {
		super(employee);
	}

	@Override
	public UUID getOwner() {
		return Employment.ownUuid;
	}
}
