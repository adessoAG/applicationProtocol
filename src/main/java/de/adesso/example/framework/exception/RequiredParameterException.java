package de.adesso.example.framework.exception;

import de.adesso.example.framework.annotation.CallingStrategy;

/**
 * The exception is thrown, if a required parameter is missing during execution
 * time. If the called bean has the {@link CallingStrategy.RequiredParameters}
 * then the calculation of this bean will be stopped. The calculation continues
 * with the remaining beans.
 *
 * @author Matthias
 *
 */
public class RequiredParameterException extends CalculationNotApplicable {

	private static final long serialVersionUID = 8134415318823607816L;

	public RequiredParameterException(final String message) {
		super(message);
	}
}
