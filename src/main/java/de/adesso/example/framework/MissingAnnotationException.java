package de.adesso.example.framework;

public class MissingAnnotationException extends RuntimeException {

	private static final long serialVersionUID = 7957386142702517862L;

	public MissingAnnotationException(final String message) {
		super(message);
	}
}
