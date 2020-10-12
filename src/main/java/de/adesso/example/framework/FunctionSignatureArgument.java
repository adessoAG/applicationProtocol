package de.adesso.example.framework;

import lombok.extern.log4j.Log4j2;

@Log4j2
public class FunctionSignatureArgument extends Argument {

	/**
	 * position of the argument on the initial call. Counts from 0.
	 */
	private final int position;

	public FunctionSignatureArgument(final Class<?> type, final int position) {
		super(type);
		this.position = position;
	}

	@Override
	protected Object prepareArgument(final ApplicationProtocol<?> state, final Object[] args) {
		if (this.position >= args.length) {
			final String message = String.format(
					"too less arguments provided. Required position %d but argument length is %d",
					this.position, args.length);
			log.atError().log(message);
			throw new IndexOutOfBoundsException(message);
		}

		return args[this.position];
	}
}
