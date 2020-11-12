package de.adesso.example.framework;

public class TestPojoBean {

	public final static String testString = "pojo test";

	public ApplicationProtocol<String> operation(final String aString, final ApplicationProtocol<String> state) {
		state.setResult(testString);
		return state;
	}

}
