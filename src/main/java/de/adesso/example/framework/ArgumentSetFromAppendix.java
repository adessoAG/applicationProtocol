package de.adesso.example.framework;

import lombok.NonNull;

public class ArgumentSetFromAppendix extends Argument {

	private final Class<? extends ApplicationAppendix<?>> appendixClass;

	public ArgumentSetFromAppendix(@NonNull final Class<?> type,
			@NonNull final Class<? extends ApplicationAppendix<?>> appendixClass) {
		super(type);
		this.appendixClass = appendixClass;
	}

	@Override
	protected Object prepareArgument(final ApplicationProtocol<?> state, final Object[] args) {
		final Object result = state.getAllAppenixesOfTypeAsSet(this.appendixClass);

		return result;
	}
}
