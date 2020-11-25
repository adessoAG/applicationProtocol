package de.adesso.example.framework.core;

import java.util.List;

import de.adesso.example.framework.ApplicationProtocol;
import lombok.NonNull;

public class ArgumentListFromAppendix extends Argument {

	public ArgumentListFromAppendix(@NonNull final Class<?> type) {
		super(type);
	}

	@Override
	protected List<?> prepareArgument(final ApplicationProtocol<?> state, final Object[] args) {
		final List<?> result = state.getAllAppenixesOfTypeAsListT(this.getType());

		this.validateArgumentCollection(result);

		return result;
	}
}
