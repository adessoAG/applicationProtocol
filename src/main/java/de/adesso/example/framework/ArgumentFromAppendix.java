package de.adesso.example.framework;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NonNull;

public class ArgumentFromAppendix extends Argument {

	@Getter(value = AccessLevel.PACKAGE)
	private final Class<? extends ApplicationAppendix<?>> appendixClass;

	public ArgumentFromAppendix(@NonNull final Class<?> type,
			@NonNull final Class<? extends ApplicationAppendix<?>> appendixClass) {
		super(type);
		this.appendixClass = appendixClass;
	}

	@Override
	protected Object prepareArgument(final ApplicationProtocol<?> state, final Object[] args) {
		Object result = state.getAppendixOfClass(this.appendixClass);
		if (result instanceof ApplicationAppendix<?>) {
			result = ((ApplicationAppendix<?>) result).getContent();
		}

		return result;
	}
}
