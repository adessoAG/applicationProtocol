package de.adesso.example.framework;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
public class ArgumentFactory {

	private final AppendixRegistry appendixRegistry;

	@Autowired
	public ArgumentFactory(final AppendixRegistry appendixRegistry) {
		this.appendixRegistry = appendixRegistry;
	}

	public Argument createArgumentByName(@NonNull final Method emulatedMethod, @NonNull final Method beanMethod,
			@NonNull final String parameterIdentifier) {
		// check possible pairs
		final ParameterPosition targetCandidate = findMatchingParametersByName(emulatedMethod,
				parameterIdentifier);
		final ParameterPosition sourceCandidate = findMatchingParametersByName(beanMethod, parameterIdentifier);

		final MethodArgument argument = new MethodArgument(targetCandidate.getParameter().getType(),
				sourceCandidate.getPosition());
		argument.setTargetPosition(targetCandidate.getPosition());
		return argument;
	}

	public Argument createArgumentByType(final Method emulatedMethod, final Method beanMethod, final Class<?> type) {
		// check possible pairs
		final ParameterPosition targetCandidate = findMatchingParametersByType(emulatedMethod, type);
		final ParameterPosition sourceCandidate = findMatchingParametersByType(beanMethod, type);

		final MethodArgument argument = new MethodArgument(targetCandidate.getParameter().getType(),
				sourceCandidate.getPosition());
		argument.setTargetPosition(targetCandidate.getPosition());
		return argument;
	}

	public Argument createArgumentFromAppendix(@NonNull final Method emulatedMethod,
			@NonNull final Class<?> parameterType) {
		final ParameterPosition targetCandidate = findMatchingParametersByType(emulatedMethod, parameterType);
		@NonNull final UUID appendixId = this.appendixRegistry.lookUp(parameterType);
		if (appendixId == null) {
			final String message = "no appropriate appendix registered. Cannot retrieve the required type";
			log.atWarn().log(message);
			throw new AppendixNotRegistered(message);
		}

		final Argument argument = new ArgumentFromAppendix(parameterType, appendixId);
		argument.setTargetPosition(targetCandidate.getPosition());

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
			throw new AbigiousParameterException(message);
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
			throw new AbigiousParameterException(message);
		}

		return relevantParameters.get(0);
	}

	@AllArgsConstructor
	@Getter
	private static class ParameterPosition {

		private final Integer position;
		private final Parameter parameter;
	}
}
