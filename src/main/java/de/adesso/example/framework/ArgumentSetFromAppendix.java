package de.adesso.example.framework;

import java.util.UUID;

import lombok.NonNull;

public class ArgumentSetFromAppendix extends Argument {
	private final UUID requiredAttachment;

	public ArgumentSetFromAppendix(@NonNull final Class<?> type, @NonNull final UUID requiredAttachment) {
		super(type);
		this.requiredAttachment = requiredAttachment;
	}

	@Override
	protected Object prepareArgument(final ApplicationProtocol<?> state, final Object[] args) {
		final Object result = state.getAllAppenixesOfTypeAsSet(this.requiredAttachment);

		return result;
	}
}
