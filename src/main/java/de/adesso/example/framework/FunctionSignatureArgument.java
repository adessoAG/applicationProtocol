package de.adesso.example.framework;

import lombok.Getter;
import lombok.extern.log4j.Log4j2;

@Getter
@Log4j2
public class FunctionSignatureArgument extends Argument {

	/**
	 * position of the argument on the initial call. Counts from 0.
	 */
	private int position;

	public FunctionSignatureArgument(Class<?> type, int position) {
		super(type);
		this.position = position;
	}

	@Override
	protected Object prepareArgument(ApplicationProtocol<?> state, Object[] args) {
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
