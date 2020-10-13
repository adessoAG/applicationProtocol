package de.adesso.example.framework;

import java.lang.reflect.Method;
import java.util.List;

import javax.validation.constraints.NotNull;

import lombok.Builder;
import lombok.Getter;
import lombok.Singular;

/**
 * This class describes an implementation for a method of a given interface. The
 * implementation may be a list of beans to be invoked. Overloading of methods
 * is not supported, because the implementation uses the method identifier to
 * distinguish between methods. The method identifier is not sufficient if
 * overloading is used.
 *
 * @author Matthias
 *
 */
public class MethodImplementation {

	/**
	 * Identifier of the method to be implemented.
	 */
	@Getter
	@NotNull
	private final String methodIdentifier;
	/**
	 * Template type of the {@link ApplicationProtocol}
	 */
	@NotNull
	private final Class<?> returnValueType;
	/**
	 * Operations to be performed to implemented that method
	 */
	@NotNull
	private final List<BeanOperation> beanOperations;
	/**
	 * Method of the interface which is implemented by this description
	 */
	@NotNull
	private Method method;

	@Builder
	private MethodImplementation(
			final String methodIdentifier,
			final Class<?> returnValueType,
			@Singular final List<BeanOperation> beanOperations) {
		this.methodIdentifier = methodIdentifier;
		this.returnValueType = returnValueType;
		this.beanOperations = beanOperations;
	}

	@SuppressWarnings("unchecked")
	public <T> ApplicationProtocol<T> execute(final ApplicationProtocol<T> state, final Object[] args) {

		ApplicationProtocol<T> intermediateState = state;

		// call all bean methods defined
		for (final BeanOperation o : this.beanOperations) {
			intermediateState = (ApplicationProtocol<T>) o.execute(intermediateState, args);
		}

		return intermediateState;
	}

	/**
	 * The method is derived from the interface. Within the interface the method is
	 * selected by the configured method identifier. Therefore this information is
	 * available later during construction of the calling syntax.
	 *
	 * @param method the implemented method
	 *
	 * @return itself for chained execution
	 */
	MethodImplementation method(final Method method) {
		this.method = method;

		return this;
	}
}
