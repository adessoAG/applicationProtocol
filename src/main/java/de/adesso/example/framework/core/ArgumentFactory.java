/**
 * The MIT License (MIT)
 *
 * Copyright © 2020 Matthias Brenner and Adesso SE
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the “Software”), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED “AS IS”, WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *
 */
package de.adesso.example.framework.core;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;

import de.adesso.example.framework.ApplicationAppendix;
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
public class ArgumentFactory {

	private final AppendixRegistry appendixRegistry;

	@Autowired
	public ArgumentFactory(final AppendixRegistry appendixRegistry) {
		this.appendixRegistry = appendixRegistry;
	}

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
		Class<? extends ApplicationAppendix<?>> appendixClass = null;
		final Parameter parameter = parameterPosition.getParameter();

		if (List.class.isAssignableFrom(parameter.getType())) {
			// parameter class is the list, need to extract the base type
			final String baseTypeName = parameter.getParameterizedType().getTypeName();
			final Class<?> parameterTypeClass = this.loadTypeClass(baseTypeName);
			appendixClass = this.lookupAppendix(parameterTypeClass);
			return new ArgumentListFromAppendix(parameterTypeClass, appendixClass);

		} else if (Set.class.isAssignableFrom(parameter.getType())) {
			// parameter class is the list, need to extract the base type
			final String baseTypeName = parameter.getParameterizedType().getTypeName();
			final Class<?> parameterTypeClass = this.loadTypeClass(baseTypeName);
			appendixClass = this.lookupAppendix(parameterTypeClass);
			return new ArgumentSetFromAppendix(parameterTypeClass, appendixClass);
		}

		// simple type
		appendixClass = this.lookupAppendix(parameter.getType());
		return new ArgumentFromAppendix(parameter.getType(), appendixClass);
	}

	private @NonNull Class<? extends ApplicationAppendix<?>> lookupAppendix(final Class<?> contentClass) {
		final Class<? extends ApplicationAppendix<?>> appendixClass = this.appendixRegistry.lookUp(contentClass);
		if (appendixClass == null) {
			throw AppendixNotRegisteredException.noAppropriateAppendix(contentClass);
		}
		return appendixClass;
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
