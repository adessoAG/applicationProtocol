package de.adesso.example.framework;

import org.springframework.stereotype.Service;

@Service
public class TestPojoBean {

	public final static String testString = "pojo test";

	public ApplicationProtocol<String> operation(final String aString, final ApplicationProtocol<String> state) {
		state.setResult(testString);
		return state;
	}

}
