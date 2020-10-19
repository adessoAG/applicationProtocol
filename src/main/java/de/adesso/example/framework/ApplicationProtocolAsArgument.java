package de.adesso.example.framework;

public class ApplicationProtocolAsArgument extends Argument {

	public ApplicationProtocolAsArgument() {
		super(ApplicationProtocol.class);
	}

	@Override
	protected Object prepareArgument(final ApplicationProtocol<?> state, final Object[] args) {

		return state;
	}
}
