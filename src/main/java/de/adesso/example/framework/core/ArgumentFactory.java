package de.adesso.example.framework.core;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.springframework.beans.factory.annotation.Autowired;

import de.adesso.example.framework.ApplicationAppendix;
import de.adesso.example.framework.exception.AppendixNotRegisteredException;
import de.adesso.example.framework.exception.UndefinedParameterException;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;
import lombok.extern.log4j.Log4j2;

/**
 * The class is responsible to create the appropriate argument.
 *
 * @author Matthias
 *
 */
@Log4j2
public class ArgumentFactory {

	private final AppendixRegistry appendixRegistry;

	@Autowired
	public ArgumentFactory(final AppendixRegistry appendixRegistry) {
		this.appendixRegistry = appendixRegistry;
	}

	/**
	 * Create an argument based on the identifier of the arguments. The type have to
	 * assignment compatible.
	 *
	 * @param emulatedMethod      the emulated method
	 * @param beanMethod          the method of the bean
	 * @param parameterIdentifier the identifier of the parameter
	 * @return the generated argument
	 * @throws UndefinedParameterException if no matching could be found
	 */
	public Argument createArgumentByName(
			@NonNull final Method emulatedMethod,
			@NonNull final Method beanMethod,
			@NonNull final String parameterIdentifier) {
		// check possible pairs
		final ParameterPosition targetCandidate = this.findMatchingParametersByName(emulatedMethod,
				parameterIdentifier);
		final ParameterPosition sourceCandidate = this.findMatchingParametersByName(beanMethod, parameterIdentifier);

		final Argument argument = new ArgumentFromMethod(targetCandidate.getParameter().getType(),
				sourceCandidate.getPosition());
		return argument;
	}

	/**
	 * Create an argument based on the type of the argument. The types have to be
	 * assignment compatible.
	 *
	 * @param emulatedMethod the emulated method
	 * @param beanMethod     the method of the bean
	 * @param type           the type of the parameter
	 * @return the generated argument
	 * @throws UndefinedParameterException if no matching could be found
	 */
	public Argument createArgumentByType(
			@NonNull final Method emulatedMethod,
			@NonNull final Method beanMethod,
			@NonNull final Class<?> type) {
		// check possible pairs
		final ParameterPosition targetCandidate = this.findMatchingParametersByType(emulatedMethod, type);
		final ParameterPosition sourceCandidate = this.findMatchingParametersByType(beanMethod, type);

		final Argument argument = new ArgumentFromMethod(targetCandidate.getParameter().getType(),
				sourceCandidate.getPosition());
		return argument;
	}

	/**
	 * Create the argument from types within the appendix. The appendix registry is
	 * queried for appendix types.
	 *
	 * @param emulatedMethod the emulated method
	 * @param parameterType  the requested type of the parameter
	 * @return the generated parameter
	 */
	public Argument createArgumentFromAppendix(
			@NonNull final Method beanmMethod,
			@NonNull final Class<?> parameterType) {
		final Class<? extends ApplicationAppendix<?>> appendixClass = this.appendixRegistry.lookUp(parameterType);
		if (appendixClass == null) {
			final String message = "no appropriate appendix registered. Cannot retrieve the required type";
			log.atWarn().log(message);
			throw new AppendixNotRegisteredException(message);
		}

		final Argument argument = new ArgumentFromAppendix(parameterType, appendixClass);

		return argument;
	}

	private ParameterPosition findMatchingParametersByName(final Method emulatedMethod,
			final String parameterIdentifier) {
		final Parameter[] emulatedParams = emulatedMethod.getParameters();
		final List<ParameterPosition> relevantParameters = IntStream.range(0, emulatedParams.length)
				.mapToObj(i -> new ParameterPosition(i, emulatedParams[i]))
				.filter(pp -> pp.getParameter().getName().equals(parameterIdentifier))
				.collect(Collectors.toList());
		if (relevantParameters.size() != 1) {
			final String message = String.format(
					"unambigious parameter matching required, there should be exactly one matching parameter. Found : %d",
					relevantParameters.size());
			log.atWarn().log(message);
			throw new UndefinedParameterException(message);
		}

		return relevantParameters.get(0);
	}

	private ParameterPosition findMatchingParametersByType(final Method emulatedMethod, final Class<?> type) {
		final Parameter[] emulatedParams = emulatedMethod.getParameters();
		final List<ParameterPosition> relevantParameters = IntStream.range(0, emulatedParams.length)
				.mapToObj(i -> new ParameterPosition(i, emulatedParams[i]))
				.filter(pp -> pp.getParameter().getType().equals(type))
				.collect(Collectors.toList());
		if (relevantParameters.size() != 1) {
			final String message = String.format(
					"unambigious parameter matching required, there should be exactly one matching parameter. Found : %d",
					relevantParameters.size());
			log.atWarn().log(message);
			throw new UndefinedParameterException(message);
		}

		return relevantParameters.get(0);
	}

	@AllArgsConstructor
	@Getter
	@EqualsAndHashCode
	@ToString
	private static class ParameterPosition {

		private final Integer position;
		private final Parameter parameter;
	}
}
