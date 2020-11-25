package de.adesso.example.framework.core;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import de.adesso.example.framework.ApplicationProtocol;
import lombok.NonNull;

public class ArgumentSetFromAppendix extends Argument {

	public ArgumentSetFromAppendix(@NonNull final Class<?> type) {
		super(type);
	}

	@Override
	protected Set<?> prepareArgument(final ApplicationProtocol<?> state, final Object[] args) {
		final Collection<?> result = state.getAllAppenixesOfTypeAsSetT(this.getType());

		this.validateArgumentCollection(result);

		return new HashSet<>(result);
	}
}
