package de.adesso.example.framework.core;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.constraints.NotNull;

import org.springframework.context.ApplicationContext;
import org.springframework.util.Assert;

import de.adesso.example.framework.ApplicationProtocol;
import de.adesso.example.framework.annotation.RequiredParameter;
import de.adesso.example.framework.exception.RequiredParameterException;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Singular;
import lombok.ToString;

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
@Getter(value = AccessLevel.PACKAGE)
@ToString
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

	private transient List<ParameterDescription> parameters;

	private transient DaisyChainDispatcher dispatcher;

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
		this.validateArgs(args);

		// call all bean methods defined
		for (final BeanOperation o : this.beanOperations) {
			try {
				intermediateState = (ApplicationProtocol<T>) o.execute(intermediateState, args);
			} catch (final RequiredParameterException e) {
				// bean method misses parameter annotated as required
				continue;
			}
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

	public void init(final DaisyChainDispatcher dispatcher, final ApplicationContext context) {
		this.beanOperations.stream().forEach(o -> o.init(this, context));
		this.dispatcher = dispatcher;

		Assert.notNull(this.method, "the method is required to initialize the handling");
		this.evaluateMethodAnnotations();
	}

	private void validateArgs(final Object[] args) {
		for (int i = 0; i < args.length; i++) {
			if (this.parameters.get(i).isRequiredParameter && args[i] == null) {
				throw new NullPointerException(String.format("required parameter at position %i is null.", i));
			}
		}
	}

	private void evaluateMethodAnnotations() {
		this.parameters = Arrays.asList(this.method.getParameters()).stream()
				.map(p -> new ParameterDescription(p.getType(), p.isAnnotationPresent(RequiredParameter.class)))
				.collect(Collectors.toList());
	}

	@Getter
	@AllArgsConstructor
	class ParameterDescription {

		private final Class<?> parameterType;
		private final boolean isRequiredParameter;
	}
}
