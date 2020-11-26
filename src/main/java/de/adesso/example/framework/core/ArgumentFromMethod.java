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

import java.lang.reflect.Parameter;

import de.adesso.example.framework.ApplicationProtocol;
import de.adesso.example.framework.exception.BuilderException;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NonNull;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class ArgumentFromMethod extends Argument {

	/**
	 * position of the argument on the initial call. Counts from 0.
	 */
	@Getter(value = AccessLevel.PACKAGE)
	private final int sourcePosition;

	public ArgumentFromMethod(final @NonNull Class<?> type, final int sourcePosition) {
		super(type);
		if (sourcePosition < 0) {
			final String message = "position may not be negative";
			log.atError().log(message);
			throw new IndexOutOfBoundsException(message);
		}
		// at this moment the Method and the parameter declarations are not available.
		// Position will be validated during initialization.
		this.sourcePosition = sourcePosition;
	}

	@Override
	void init(final BeanOperation beanOperation, final Parameter parameter, final int targetPosition) {
		super.init(beanOperation, parameter, targetPosition);
		this.validateSourcePosition();
	}

	@Override
	protected Object prepareArgument(final ApplicationProtocol<?> state, final Object[] args) {
		final Object parameter = args[this.sourcePosition];
		this.validateArgument(parameter);
		return parameter;
	}

	private void validateSourcePosition() {
		if (this.sourcePosition > this.getBeanOperation().getMethodImplementation().getMethod()
				.getParameters().length) {
			throw BuilderException.sourcePositionOutOfRange(this.getBean(), this.getBeanMethodIdentifier(),
					this.getBeanOperation().getMethodImplementation().getDispatcher().getImplementationInterface());
		}

		if (!this.getSourceParameter().getClass().isAssignableFrom(this.getTargetParameter())) {
			throw BuilderException.parameterNotAssignable(
					this.getEmulatedInterface(),
					this.getEmulatedInterfaceMethod(),
					this.getBean(),
					this.getBeanOperation().getMethodIdentifier(),
					this.getParameterName());
		}
	}

	private Parameter getSourceParameter() {
		return this.getBeanOperation().getMethodImplementation().getMethod().getParameters()[this.sourcePosition];
	}
}
