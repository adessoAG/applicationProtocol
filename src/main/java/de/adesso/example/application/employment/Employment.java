/**
 * The MIT License (MIT)
 *
 * Copyright © 2020 Matthias Brenner and Adesso SE
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the “Software”), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED “AS IS”, WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *
 */
package de.adesso.example.application.employment;

import java.util.UUID;

import org.springframework.stereotype.Service;

import de.adesso.example.application.accounting.Creditor;
import de.adesso.example.framework.ApplicationOwner;

@Service
public class Employment extends ApplicationOwner {

	public static final UUID ownUuid = UUID.randomUUID();

	private static final Creditor employeeDiscountCreditor = new Creditor(UUID.randomUUID());

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

	public void registerNonCashBenefit(final EmployeeBenefit benefit) {
		benefit.getEmployee().registerBenefit(benefit);
	}

	public static Creditor getEmployeeDiscountCreditor() {
		return employeeDiscountCreditor;
	}
}
