package de.adesso.example.framework.annotation;

/**
 * Which calling strategy should be applied.
 *
 * @author Matthias
 *
 */
public enum CallingStrategy {
	/**
	 * Call the method in any case. If required parameters are not present, an
	 * exception is thrown.
	 */
	Eager,
	/**
	 * Call the method if the required parameters can be provided. Otherwise the
	 * method is not called. The call is skipped without any error message. This
	 * behavior can be used, if the call depends on information provided.
	 */
	RequiredParameters
}
