package de.adesso.example.framework;

public interface TestSpringBean {

	public ApplicationProtocol<String> operation(final String aString, final ApplicationProtocol<String> state);
}
