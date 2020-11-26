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
package de.adesso.example.framework.exception;

import java.lang.reflect.Method;

import lombok.NonNull;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class BuilderException extends RuntimeException {

	private static final long serialVersionUID = -8390406591314015878L;

	private BuilderException(final String message, final Exception e) {
		super(message, e);
	}

	private BuilderException(final String message) {
		super(message);
	}

	public static BuilderException notEnoughInformationAvailable(final Class<?> emulationClass,
			final String methodIdentifier) {
		final String message = String.format("no type given for implementation of bean. emulation of %s::%s",
				emulationClass.getName(),
				methodIdentifier);
		log.atError().log(message);

		return new BuilderException(message);
	}

	public static BuilderException missingEmptyConstructor(final Class<?> beanType,
			final String methodIdentifier) {
		final String message = String.format(
				"cannot load bean, no empty constructor found %s::%s",
				beanType.getName(),
				methodIdentifier);
		log.atError().log(message);
		return new BuilderException(message);
	}

	public static BuilderException missingType(final Class<?> beanType, final String methodIdentifier) {
		final String message = String.format(
				"missing bean class on emulation %s::%s",
				beanType.getName(),
				methodIdentifier);
		log.atError().log(message);
		return new BuilderException(message);
	}

	public static BuilderException classNotLoaded(final Class<Object> beanType, final Exception e) {
		final String message = String.format(
				"failed to load class %s", beanType);
		log.atError().log(message);
		log.atError().log(e);
		return new BuilderException(message, e);
	}

	public static BuilderException methodNotFound(final Class<?> describingClass, final String methodIdentifier,
			final Exception e) {
		final String message = String.format("could not build bean operation %s::%s", describingClass.getName(),
				methodIdentifier);
		log.atError().log(message);
		log.atError().log(e);
		return new BuilderException(message, e);
	}

	public static BuilderException methodNotFound(@NonNull final Class<?> implClass,
			@NonNull final String methodName) {
		final String message = String.format("could not locate the requested method %s::%s",
				implClass.getName(), methodName);
		log.atError().log(message);
		return new BuilderException(message);
	}

	public static BuilderException cannotLoadAppendixClass(final String typeName,
			final ClassNotFoundException e) {
		final String message = String.format("problem should never happen, could not load class %s", typeName);
		log.error(message, e);
		return new BuilderException(message, e);
	}

	public static BuilderException sourcePositionOutOfRange(final Class<?> bean,
			final String beanMethodIdentifier,
			final Class<?> implementationInterface) {
		final String message = String.format(
				"parameter position is greater as the method parameters (%s::%s) available from the describing interface(%s)",
				bean.getName(),
				beanMethodIdentifier,
				implementationInterface.getName());
		log.atError().log(message);
		return new BuilderException(message);
	}

	public static BuilderException parameterNotAssignable(
			final Class<?> emulatedInterface,
			final Method emulatedInterfaceMethod,
			final Class<?> bean,
			final String methodIdentifier,
			final String parameterName) {
		final String message = String.format(
				"parameter %s of interface %s::%s cannot be assigned to bean %s::%s.",
				parameterName,
				emulatedInterface.getName(),
				emulatedInterfaceMethod.getName(),
				bean.getName(),
				methodIdentifier);
		log.atError().log(message);
		return new BuilderException(message);
	}

	public static BuilderException classNotLoaded(final String className, final Exception e) {
		final String message = String.format("problem should never happen, could not load class %s", className);
		log.error(message);
		log.error(e);
		return new BuilderException(message, e);
	}

	public static BuilderException appendixConventionBroken(final Class<?> beanClass) {
		final String message = String.format(
				"Appendixes are required to subclass ApplicationAppendix, please check class %s",
				beanClass.getName());
		log.atError().log(message);
		return new BuilderException(message);
	}
}
