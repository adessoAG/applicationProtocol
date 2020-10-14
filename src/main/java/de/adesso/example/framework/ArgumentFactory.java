package de.adesso.example.framework;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
public class ArgumentFactory {

	public Argument createArgumentByName(@NonNull final Method emulatedMethod, @NonNull final Method beanMethod,
			@NonNull final String parameterIdentifier) {
		// check possible pairs
		final List<ParameterPosition> targetCandidates = findMatchingParametersByName(emulatedMethod, parameterIdentifier);
		final List<ParameterPosition> sourceCandidates = findMatchingParametersByName(beanMethod, parameterIdentifier);

		final MethodArgument argument = new MethodArgument(targetCandidates.get(0).getParameter().getType(),
				sourceCandidates.get(0).getPosition());
		argument.setTargetPosition(targetCandidates.get(0).getPosition());
		return argument;
	}

	public Argument createArgumentByType(final Method emulatedMethod, final Method beanMethod, final Class<?> type) {
		return null;
	}

	private List<ParameterPosition> findMatchingParametersByName(final Method emulatedMethod,
			final String parameterIdentifier) {
		final Parameter[] emulatedParams = emulatedMethod.getParameters();
		final List<ParameterPosition> relevantParameters = IntStream.range(0, emulatedParams.length)
				.mapToObj(i -> new ParameterPosition(i, emulatedParams[i]))
				.filter(pp -> pp.getParameter().getName().equals(parameterIdentifier))
				.collect(Collectors.toList());
		if (relevantParameters.size() != 1) {
			final String message = String.format(
					"unambigious parameter matching required, there should be exactly one matching parameter. Found :",
					relevantParameters.size());
			log.atWarn().log(message);
			throw new AbigiousParameterException(message);
		}

		return relevantParameters;
	}

	@AllArgsConstructor
	@Getter
	private static class ParameterPosition {

		private final Integer position;
		private final Parameter parameter;
	}
}
