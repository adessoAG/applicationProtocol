package de.adesso.example.framework.core;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

import de.adesso.example.framework.ApplicationAppendix;
import de.adesso.example.framework.ApplicationProtocol;
import lombok.NonNull;

public class ArgumentSetFromAppendix extends Argument {

	private final Class<? extends ApplicationAppendix<?>> appendixClass;

	public ArgumentSetFromAppendix(@NonNull final Class<?> type,
			@NonNull final Class<? extends ApplicationAppendix<?>> appendixClass) {
		super(type);
		this.appendixClass = appendixClass;
	}

	@Override
	protected Set<?> prepareArgument(final ApplicationProtocol<?> state, final Object[] args) {
		final Collection<ApplicationAppendix<?>> result = state.getAllAppenixesOfTypeAsSet(this.appendixClass);

		this.validateArgumentCollection(result);

		return result.stream()
				.map(a -> a.getContent())
				.collect(Collectors.toSet());
	}
}
