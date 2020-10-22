package de.adesso.example.application.employment;

import java.util.UUID;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import de.adesso.example.framework.ApplicationOwner;
import de.adesso.example.framework.AppendixRegistry;
import de.adesso.example.framework.TApplicationAppendix;

@Service
public class Employment extends ApplicationOwner {

	public final static UUID ownUuid = UUID.randomUUID();
	public static UUID employeeAppendixId = UUID.randomUUID();

	private final AppendixRegistry registry;

	@Autowired
	public Employment(final AppendixRegistry registry) {
		super(ownUuid);

		this.registry = registry;
	}

	@PostConstruct
	private void setup() {
		this.registry.register(employeeAppendixId, Employee.class);
	}

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

	/**
	 * create an appendix containing an employee type
	 *
	 * @param employee the employee to be wrapped within an appendix
	 * @return the appendix containing the employee
	 */
	public TApplicationAppendix<Employee> createEmployeeAppendix(final Employee employee) {
		return new TApplicationAppendix<>(employeeAppendixId, getOwnUuid(), employee);
	}
}
