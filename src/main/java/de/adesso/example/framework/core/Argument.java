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
import java.util.Collection;
import java.util.Optional;

import de.adesso.example.framework.ApplicationProtocol;
import de.adesso.example.framework.annotation.Required;
import de.adesso.example.framework.exception.RequiredParameterException;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.ToString;

@Getter(value = AccessLevel.PACKAGE)
@ToString
public abstract class Argument {

	/**
	 * type of the parameter of the implementing bean
	 */
	private final Class<?> type;

	/**
	 * Position of the parameter for the call of the bean implementing the method
	 * call. There are several procedures to create the argument list, this
	 * information will be set by the {@link MethodImplementation} during
	 * initialization. Since the implementing class might be a bean which is
	 */
	private int targetPosition;

	/**
	 * If the parameter is annotated as required, this flag will be set.
	 */
	private boolean required;

	/**
	 * If the parameter is required, it may be specified, that a collection should
	 * not be empty.
	 */
	private boolean requiredNotEmpty;

	private transient String parameterName;
	private transient BeanOperation beanOperation;

	public Argument(final Class<?> type) {
		this.type = type;
	}

	protected abstract Object prepareArgument(ApplicationProtocol<?> state, Object[] args);

	/**
	 * evaluate the parameters of the method and read the declared annotations. The
	 * annotations influence the behavior of execution. Every specific
	 * implementation of argument may overwrite this method and call super to set
	 * the field targetPosition.
	 *
	 * @param parameters
	 */
	void init(final BeanOperation beanOperation, final Parameter parameter, final int targetPosition) {
		this.targetPosition = targetPosition;
		this.parameterName = parameter.getName();
		this.beanOperation = beanOperation;

		this.checkRequiredAnnotation(parameter);
	}

	private void checkRequiredAnnotation(final Parameter parameter) {
		final Required annotation = parameter.getAnnotation(Required.class);
		this.required = annotation != null;
		if (this.required) {
			this.requiredNotEmpty = annotation.requireNotEmpty();
		}
	}

	protected void validateArgument(final Optional<?> parameterOptional) {
		if (!this.isRequired()) {
			return;
		}
		// parameter is required
		if (parameterOptional.isEmpty()) {
			throw RequiredParameterException.parameterMissing(this.parameterName, this.beanOperation.getBeanType(),
					this.parameterName);
		}
		// parameter is present
		final Object parameter = parameterOptional.get();
		if (Collection.class.isAssignableFrom(parameter.getClass())
				&& this.requiredNotEmpty
				&& ((Collection<?>) parameter).isEmpty()) {
			throw RequiredParameterException.collectionEmpty(this.parameterName, this.beanOperation.getBeanType(),
					this.parameterName);
		}
	}

	protected void validateArgument(final Object parameter) {
		if (!this.isRequired()) {
			return;
		}
		// parameter is required
		if (parameter == null) {
			throw RequiredParameterException.parameterMissing(this.parameterName, this.beanOperation.getBeanType(),
					this.parameterName);
		}
		// parameter is present
		if (Collection.class.isAssignableFrom(parameter.getClass())
				&& this.requiredNotEmpty
				&& ((Collection<?>) parameter).isEmpty()) {
			throw RequiredParameterException.collectionEmpty(this.parameterName, this.beanOperation.getBeanType(),
					this.parameterName);
		}
	}

	protected void validateArgument(final ApplicationProtocol<?> parameter) {
		if (this.isRequired() && parameter == null) {
			throw RequiredParameterException.parameterMissing(this.parameterName, this.beanOperation.getBeanType(),
					this.parameterName);
		}
		if (parameter == null) {
			return;
		}
		if (Collection.class.isAssignableFrom(parameter.getClass())
				&& this.requiredNotEmpty
				&& ((Collection<?>) parameter).isEmpty()) {
			throw RequiredParameterException.collectionEmpty(this.parameterName, this.beanOperation.getBeanType(),
					this.parameterName);
		}
	}

	protected void validateArgumentCollection(final Collection<?> argumentCollection) {
		if (this.isRequired() && argumentCollection == null) {
			throw RequiredParameterException.collectionEmpty(this.parameterName, this.beanOperation.getBeanType(),
					this.parameterName);
		}
		if (this.requiredNotEmpty && argumentCollection.isEmpty()) {
			throw RequiredParameterException.collectionEmpty(this.parameterName, this.beanOperation.getBeanType(),
					this.parameterName);
		}
	}

	protected Class<?> getBean() {
		return this.getBeanOperation().getBeanType();
	}

	protected String getBeanMethodIdentifier() {
		return this.getBeanOperation().getMethodIdentifier();
	}

	protected Class<?> getEmulatedInterface() {
		return this.getBeanOperation().getMethodImplementation().getDispatcher().getImplementationInterface();
	}

	protected Method getEmulatedInterfaceMethod() {
		return this.getBeanOperation().getMethodImplementation().getMethod();
	}

	protected Class<? extends Parameter> getTargetParameter() {
		return this.getBeanOperation().getMethod().getParameters()[this.getTargetPosition()].getClass();
	}
}