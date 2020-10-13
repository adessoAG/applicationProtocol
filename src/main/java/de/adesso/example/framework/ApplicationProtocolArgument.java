package de.adesso.example.framework;

public class ApplicationProtocolArgument extends Argument {

	public ApplicationProtocolArgument() {
		super(ApplicationProtocol.class);
	}

	@Override
	protected Object prepareArgument(final ApplicationProtocol<?> state, final Object[] args) {

		return state;
	}
}
