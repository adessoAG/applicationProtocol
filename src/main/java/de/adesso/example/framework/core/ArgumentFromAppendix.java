package de.adesso.example.framework.core;

import java.util.Optional;

import de.adesso.example.framework.ApplicationAppendix;
import de.adesso.example.framework.ApplicationProtocol;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;

@ToString
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
		final Optional<ApplicationAppendix<?>> appendixOfClass = state.getAppendixOfClass(this.appendixClass);
		final Optional<? extends ApplicationAppendix<?>> appendix = appendixOfClass;

		this.validateArgument(appendix);

		return appendix.isEmpty() ? null : appendix.get().getContent();
	}
}
