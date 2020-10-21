package de.adesso.example.framework.exception;

public class BuilderException extends RuntimeException {

	private static final long serialVersionUID = -8390406591314015878L;

	public BuilderException(final String message, final Exception e) {
		super(message, e);
	}
}
