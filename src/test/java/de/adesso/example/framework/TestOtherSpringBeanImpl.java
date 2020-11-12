package de.adesso.example.framework;

import org.springframework.stereotype.Service;

@Service
public class TestOtherSpringBeanImpl {

	public final static String testString = "other testing";

	public ApplicationProtocol<String> operation(final ApplicationProtocol<String> state) {
		state.setResult(testString);
		return state;
	}

}
