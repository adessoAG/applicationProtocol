package de.adesso.example.framework;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public abstract class Argument {
	protected Class<?> type;

	protected abstract Object prepareArgument(ApplicationProtocol<?> state, Object[] args);
}