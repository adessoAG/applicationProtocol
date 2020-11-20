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
