package de.adesso.example.framework.exception;

import de.adesso.example.framework.annotation.CallStrategy;
import de.adesso.example.framework.annotation.CallingStrategy;

/**
 * If a method of a bean is marked with the {@link CallStrategy} with the value
 * {@link CallingStrategy#REQUIRED_PARAMETER} then the calculation can be
 * skipped by throwing this exception or their specializations. The remaining
 * beans will be evaluated.
 *
 * @author Matthias
 *
 */
public class CalculationNotApplicable extends RuntimeException {

	private static final long serialVersionUID = 8984977208834502097L;

	public CalculationNotApplicable(final String message) {
		super(message);
	}
}
