package de.adesso.example.application.employment;

import java.util.UUID;

import org.springframework.stereotype.Service;

import de.adesso.example.framework.ApplicationOwner;

@Service
public class Employment extends ApplicationOwner {

	public final static UUID ownUuid = UUID.randomUUID();

	// employee factory methods

	/**
	 * create an employee
	 *
	 * @param name      name of the employee
	 * @param firstName first name of the employee
	 * @param id        internal id of the employee
	 * @return the employee type
	 */
	public Employee createEmployee(final String name, final String firstName, final int id) {
		return new Employee(name, firstName, id);
	}

	/**
	 * Look up an employee by its id
	 *
	 * @param id the id of the employee
	 * @return the employee type
	 */
	public Employee lookup(final int id) {
		return null;
	}

	@Override
	protected UUID getOwnerId() {
		return ownUuid;
	}
}
