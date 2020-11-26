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

import java.lang.reflect.Parameter;

import lombok.extern.log4j.Log4j2;

@Log4j2
public class AppendixNotRegisteredException extends RuntimeException {

	private AppendixNotRegisteredException(final String message) {
		super(message);
	}

	private static final long serialVersionUID = 5857742183468660324L;

	public static AppendixNotRegisteredException noParameterMatch(final Parameter param) {
		final String message = String.format("could not match parameter %s %s", param.getType(), param.getName());
		log.atError().log(message);

		return new AppendixNotRegisteredException(message);
	}

	public static AppendixNotRegisteredException noAppropriateAppendix(final Class<?> type) {
		final String message = String.format("no appropriate appendix for type %s registered.", type.getName());
		log.atError().log(message);

		return new AppendixNotRegisteredException(message);
	}
}
