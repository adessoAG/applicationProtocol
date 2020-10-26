package de.adesso.example.framework;

import lombok.NonNull;

public class ArgumentListFromAppendix extends Argument {

	private final Class<? extends ApplicationAppendix<?>> appendixClass;

	public ArgumentListFromAppendix(@NonNull final Class<?> type,
			@NonNull final Class<? extends ApplicationAppendix<?>> appendixClass) {
		super(type);
		this.appendixClass = appendixClass;
	}

	@Override
	protected Object prepareArgument(final ApplicationProtocol<?> state, final Object[] args) {
		final Object result = state.getAllAppenixesOfTypeAsList(this.appendixClass);

		return result;
	}
}
