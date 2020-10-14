package de.adesso.example.framework;

import lombok.Getter;
import lombok.Setter;

@Getter
public abstract class Argument {

	protected final Class<?> type;
	@Setter
	private int targetPosition;

	public Argument(final Class<?> type) {
		this.type = type;
	}

	public Argument(final Class<?> type, final int targetPosition) {
		this.type = type;
		this.targetPosition = targetPosition;
	}

	protected abstract Object prepareArgument(ApplicationProtocol<?> state, Object[] args);
}