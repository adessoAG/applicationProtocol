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

import de.adesso.example.framework.annotation.CallingStrategy;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;

/**
 * The exception is thrown, if a required parameter is missing during execution
 * time. If the called bean has the {@link CallingStrategy#REQUIRED_PARAMETER}
 * then the calculation of this bean will be stopped. The calculation continues
 * with the remaining beans.
 *
 * @author Matthias
 *
 */
@Log4j2
public class RequiredParameterException extends CalculationNotApplicable {

	private static final long serialVersionUID = 8134415318823607816L;
	@Getter
	private final String method;

	private RequiredParameterException(final String message, final String method) {
		super(message);
		this.method = method;
	}

	public static RequiredParameterException parameterMissing(final String parameterName,
			final Class<Object> beanType, final String method) {
		final String message = String.format("required parameter %s not found, will not call the bean %s",
				parameterName, beanType);
		log.atDebug().log(message);
		return new RequiredParameterException(message, method);
	}

	public static RequiredParameterException collectionEmpty(final String parameterName, final Class<Object> beanType,
			final String method) {
		final String message = String.format("collection required not empty, but was: parameter %s, %s",
				parameterName, beanType);
		log.atDebug().log(message);
		return new RequiredParameterException(message, method);
	}
}
