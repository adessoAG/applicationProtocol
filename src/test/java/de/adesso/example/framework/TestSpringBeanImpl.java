package de.adesso.example.framework;

import org.springframework.stereotype.Service;

@Service
public class TestSpringBeanImpl implements TestSpringBean {

	public ApplicationProtocol<String> operation(final String aString, final ApplicationProtocol<String> state) {
		state.setResult(aString);
		return state;
	}

}
