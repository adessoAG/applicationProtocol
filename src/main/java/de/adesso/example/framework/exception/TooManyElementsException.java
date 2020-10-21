package de.adesso.example.framework.exception;

public class TooManyElementsException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	
	public TooManyElementsException(String message) {
		super(message);
	}

}
