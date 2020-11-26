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

import lombok.extern.log4j.Log4j2;

@Log4j2
public class BeanCallException extends RuntimeException {

	private static final long serialVersionUID = -8390406591314015878L;

	private BeanCallException(final String message, final Throwable e) {
		super(message, e);
	}

	private BeanCallException(final String message) {
		super(message);
	}

	public static BeanCallException callFailedWithException(final Class<?> beanClass, final Method method,
			final Throwable th) {
		final String message = String.format("bean invocation through exception: %s::%s -> %s",
				beanClass.getClass().getName(),
				method.getName(),
				th.getClass().getName());
		log.atError().log(message);
		log.atError().log(th);

		return new BeanCallException(message, th);
	}

	public static BeanCallException callFailedMissingParameter(final Class<?> beanClass, final Method method,
			final String parameterName) {
		final String message = String.format("bean invocation required parameters, but missing: %s::%s -> %s",
				beanClass.getClass().getName(),
				method.getName(),
				parameterName);
		log.atError().log(message);

		return new BeanCallException(message);
	}

	public static BeanCallException callFailedOnStrategy(final Class<?> beanClass, final Method method) {
		final String message = String.format("bean invocation failed due to strategy: %s::%s",
				beanClass.getClass().getName(),
				method.getName());
		log.atError().log(message);

		return new BeanCallException(message);
	}
}
