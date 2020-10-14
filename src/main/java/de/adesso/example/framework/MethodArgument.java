package de.adesso.example.framework;

import javax.validation.constraints.PositiveOrZero;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NonNull;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class MethodArgument extends Argument {

	/**
	 * position of the argument on the initial call. Counts from 0.
	 */
	@Getter(value = AccessLevel.PACKAGE)
	private final int sourcePosition;

	public MethodArgument(final @NonNull Class<?> type, final @PositiveOrZero int sourcePosition) {
		super(type);
		if (sourcePosition < 0) {
			final String message = "position may not be negative";
			log.atError().log(message);
			throw new IndexOutOfBoundsException(message);
		}
		this.sourcePosition = sourcePosition;
	}

	@Override
	protected Object prepareArgument(final ApplicationProtocol<?> state, final Object[] args) {
		if (this.sourcePosition >= args.length) {
			final String message = String.format(
					"too less arguments provided. Required position %d but last possible position is %d",
					this.sourcePosition, args.length - 1);
			log.atError().log(message);
			throw new IndexOutOfBoundsException(message);
		}

		return args[this.sourcePosition];
	}
}
