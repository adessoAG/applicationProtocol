package de.adesso.example.framework.exception;

public class UndefinedParameterException extends RuntimeException {

	private static final long serialVersionUID = 7189748745339976151L;

	public UndefinedParameterException(final String message) {
		super(message);
	}
}
