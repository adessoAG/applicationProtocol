package de.adesso.example.framework;

import java.util.UUID;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NonNull;

public class ArgumentFromAppendix extends Argument {

	@Getter(value = AccessLevel.PACKAGE)
	private final UUID appendixId;

	public ArgumentFromAppendix(@NonNull final Class<?> type, @NonNull final UUID appendixId) {
		super(type);
		this.appendixId = appendixId;
	}

	@Override
	protected Object prepareArgument(final ApplicationProtocol<?> state, final Object[] args) {
		Object result = state.getAppendixOfType(this.appendixId);
		if (result instanceof ApplicationAppendix<?>) {
			result = ((ApplicationAppendix<?>) result).getContent();
		}

		return result;
	}
}
