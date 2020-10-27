package de.adesso.example.framework;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import de.adesso.example.framework.exception.BuilderException;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.Singular;
import lombok.extern.log4j.Log4j2;

/**
 * This class describes how a method of a class should be called within the
 * framework. The requirements of the framework are that the result type is
 * {@link ApplicationProtocol}. There may be some parameters which can be given
 * from the call by {@link MethodArgument} or from the list of appendixes by the
 * class {@link ArgumentFromAppendix}.
 *
 * @author Matthias
 *
 */
@Getter
@Log4j2
public class BeanOperation {

	// identifier of the operation
	@NonNull
	private final String methodIdentifier;
	// object which provides the requested method
	@NonNull
	private final ApplicationFrameworkInvokable implementation;
	// Operation list to be executed
	@NonNull
	private final Method method;
	// parameters
	private final List<Argument> arguments;

	@Builder
	private BeanOperation(final String methodIdentifier, final ApplicationFrameworkInvokable implementation,
			@Singular final List<Argument> arguments) {
		this.methodIdentifier = methodIdentifier;
		this.implementation = implementation;
		this.arguments = arguments;

		// provide the target position
		IntStream.range(0, arguments.size()).forEach(i -> arguments.get(i).setTargetPosition(i));

		try {
			log.atDebug().log("going to extract method {}::{}", implementation.getClass().getName(), methodIdentifier);
			this.method = implementation.getClass().getDeclaredMethod(methodIdentifier, argumentTypes(arguments));
		} catch (NoSuchMethodException | SecurityException e) {
			final String message = "could not build bean operation";
			log.atError().log(message, e);
			throw new BuilderException(message, e);
		}
	}

	/**
	 * execute the described method.
	 *
	 * @param state application protocol instance
	 * @param args  arguments
	 * @return the updated application protocol instance
	 */
	public ApplicationProtocol<?> execute(final ApplicationProtocol<?> state, final Object[] args) {

		final Object[] methodArguments = prepareArguments(state, args);
		ApplicationProtocol<?> result = null;
		try {
			result = (ApplicationProtocol<?>) this.method.invoke(this.implementation, methodArguments);

		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			final String message = String.format("could not invoke configured target bean (%s::%s)",
					this.implementation.getClass().getName(), this.method.getName());
			log.atError().log(message);
			throw new ClassCastException(message);
		}

		return result;
	}

	private Class<?>[] argumentTypes(final List<Argument> arguments) {
		final List<Class<?>> resultTypes = arguments.stream()
				.map(Argument::getType)
				.collect(Collectors.toList());
		return resultTypes.toArray(new Class<?>[resultTypes.size()]);
	}

	private Object[] prepareArguments(final ApplicationProtocol<?> state, final Object[] args) {
		final Object[] result = this.arguments.stream()
				.map(a -> a.prepareArgument(state, args))
				.collect(Collectors.toList()).toArray();

		return result;
	}
}
