package de.adesso.example.framework.core;

import java.util.Optional;

import de.adesso.example.framework.ApplicationProtocol;
import lombok.NonNull;
import lombok.ToString;

@ToString
public class ArgumentOptionalFromAppendix extends Argument {

	public ArgumentOptionalFromAppendix(@NonNull final Class<?> type) {
		super(type);
	}

	@Override
	protected Optional<?> prepareArgument(final ApplicationProtocol<?> state, final Object[] args) {
		final Optional<?> optionalAppendix = state.getAppendixOfClassT(this.getType());

		this.validateArgument(optionalAppendix);

		return optionalAppendix;
	}
}
