package de.adesso.example.framework.core;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
@ToString
class ParameterPosition {

	private final int position;
	private final Parameter parameter;

	static List<ParameterPosition> buildParameterList(final Method method) {

		final Parameter[] beanMethodParameters = method.getParameters();
		final List<ParameterPosition> beanParamPositions = IntStream.range(0, beanMethodParameters.length)
				.mapToObj(i -> new ParameterPosition(i, beanMethodParameters[i]))
				.collect(Collectors.toList());
		return beanParamPositions;

	}
}
