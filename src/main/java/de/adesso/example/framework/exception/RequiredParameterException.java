package de.adesso.example.framework.exception;

public class RequiredParameterException extends RuntimeException {

	private static final long serialVersionUID = 8134415318823607816L;

	public RequiredParameterException(final String message) {
		super(message);
	}
}
