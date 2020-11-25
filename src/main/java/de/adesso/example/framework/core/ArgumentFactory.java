package de.adesso.example.framework.core;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import de.adesso.example.framework.exception.AppendixNotRegisteredException;
import de.adesso.example.framework.exception.BuilderException;
import de.adesso.example.framework.exception.UndefinedParameterException;
import lombok.NonNull;

/**
 * The class is responsible to create the appropriate argument. This class is
 * not annotated as service, but the post-processor announces it to the context.
 *
 * @author Matthias
 *
 */
@Service
public class ArgumentFactory {

	/**
	 * Create an argument based on the type of the argument. The types have to be
	 * assignment compatible.
	 *
	 * @param emulatedMethod   the emulated method
	 * @param beanMethod       the method of the bean
	 * @param paramterPosition the type and position of the parameter
	 * @return the generated argument
	 * @throws UndefinedParameterException if no matching could be found
	 */
	public @NonNull Argument createArgument(
			@NonNull final Method emulatedMethod,
			@NonNull final Method beanMethod,
			@NonNull final ParameterPosition paramterPosition) {

		// check parameters of the call
		final Class<?> beanParameterType = paramterPosition.getParameter().getType();
		List<ParameterPosition> candidates = ParameterPosition.buildParameterList(emulatedMethod).stream()
				.filter(emuParam -> beanParameterType.isAssignableFrom(emuParam.getParameter().getType()))
				.collect(Collectors.toList());
		if (candidates.isEmpty()) {
			return this.createAppendixArgument(paramterPosition);
		}

		if (candidates.size() == 1) {
			// unique result
			return this.buildArgumtParameter(paramterPosition, candidates.get(0));
		} else {
			// try to match with parameter identifiers
			candidates = candidates.stream()
					.filter(emuParam -> paramterPosition.getParameter().getName()
							.equals(emuParam.getParameter().getName()))
					.collect(Collectors.toList());
			if (candidates.size() == 1) {
				// unique result
				return this.buildArgumtParameter(paramterPosition, candidates.get(0));
			}
			// no unique result achieved, throw exception
			throw AppendixNotRegisteredException.noParameterMatch(paramterPosition.getParameter());
		}
	}

	private @NonNull Argument buildArgumtParameter(@NonNull final ParameterPosition beanParamterPosition,
			final ParameterPosition emulationParameterPosition) {
		return new ArgumentFromMethod(beanParamterPosition.getParameter().getType(),
				emulationParameterPosition.getPosition());
	}

	private @NonNull Argument createAppendixArgument(@NonNull final ParameterPosition parameterPosition) {
		final Parameter parameter = parameterPosition.getParameter();

		if (List.class.isAssignableFrom(parameter.getType())) {
			// parameter class is the list, need to extract the base type
			final String baseTypeName = parameter.getParameterizedType().getTypeName();
			final Class<?> parameterTypeClass = this.loadTypeClass(baseTypeName);

			return new ArgumentListFromAppendix(parameterTypeClass);

		} else if (Set.class.isAssignableFrom(parameter.getType())) {
			// parameter class is the list, need to extract the base type
			final String baseTypeName = parameter.getParameterizedType().getTypeName();
			final Class<?> parameterTypeClass = this.loadTypeClass(baseTypeName);

			return new ArgumentSetFromAppendix(parameterTypeClass);

		} else if (Optional.class.isAssignableFrom(parameter.getType())) {
			// parameter class is the list, need to extract the base type
			final String baseTypeName = parameter.getParameterizedType().getTypeName();
			final Class<?> parameterTypeClass = this.loadTypeClass(baseTypeName);

			return new ArgumentOptionalFromAppendix(parameterTypeClass);
		}

		// simple type
		return new ArgumentFromAppendix(parameter.getType());
	}

	private Class<?> loadTypeClass(final String className) {
		final int begin = className.indexOf('<');
		final int end = className.indexOf('>');
		String name = className;
		if (begin >= 0 && end >= 0) {
			name = className.substring(begin + 1, end);
		}
		Class<?> classForName = null;
		try {
			classForName = ArgumentFactory.class.getClassLoader().loadClass(name);
		} catch (final ClassNotFoundException e) {
			// should never happen, because the type is part of the class variable we
			// received as parameter.
			throw BuilderException.classNotLoaded(className, e);
		}
		return classForName;
	}
}
