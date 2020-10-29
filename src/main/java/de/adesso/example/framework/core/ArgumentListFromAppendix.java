package de.adesso.example.framework.core;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import de.adesso.example.framework.ApplicationAppendix;
import de.adesso.example.framework.ApplicationProtocol;
import lombok.NonNull;

public class ArgumentListFromAppendix extends Argument {

	private final Class<? extends ApplicationAppendix<?>> appendixClass;

	public ArgumentListFromAppendix(@NonNull final Class<?> type,
			@NonNull final Class<? extends ApplicationAppendix<?>> appendixClass) {
		super(type);
		this.appendixClass = appendixClass;
	}

	@Override
	protected List<Object> prepareArgument(final ApplicationProtocol<?> state, final Object[] args) {
		final Collection<ApplicationAppendix<?>> result = state.getAllAppenixesOfTypeAsList(this.appendixClass);

		this.validateArgumentCollection(result);

		return result.stream()
				.map(a -> a.getContent())
				.collect(Collectors.toList());
	}
}
