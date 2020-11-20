package de.adesso.example.framework.exception;

import de.adesso.example.framework.annotation.CallingStrategy;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;

/**
 * The exception is thrown, if a required parameter is missing during execution
 * time. If the called bean has the {@link CallingStrategy#REQUIRED_PARAMETER}
 * then the calculation of this bean will be stopped. The calculation continues
 * with the remaining beans.
 *
 * @author Matthias
 *
 */
@Log4j2
public class RequiredParameterException extends CalculationNotApplicable {

	private static final long serialVersionUID = 8134415318823607816L;
	@Getter
	private final String method;

	private RequiredParameterException(final String message, final String method) {
		super(message);
		this.method = method;
	}

	public static RequiredParameterException parameterMissing(final String parameterName,
			final Class<Object> beanType, final String method) {
		final String message = String.format("required parameter %s not found, will not call the bean %s",
				parameterName, beanType);
		log.atDebug().log(message);
		return new RequiredParameterException(message, method);
	}

	public static RequiredParameterException collectionEmpty(final String parameterName, final Class<Object> beanType,
			final String method) {
		final String message = String.format("collection required not empty, but was: parameter %s, %s",
				parameterName, beanType);
		log.atDebug().log(message);
		return new RequiredParameterException(message, method);
	}
}
