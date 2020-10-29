package de.adesso.example.framework.core;

import de.adesso.example.framework.ApplicationProtocol;

public class ArgumentApplicationProtocol extends Argument {

	public ArgumentApplicationProtocol() {
		super(ApplicationProtocol.class);
	}

	@Override
	protected Object prepareArgument(final ApplicationProtocol<?> state, final Object[] args) {

		validateArgument(state);

		return state;
	}
}
