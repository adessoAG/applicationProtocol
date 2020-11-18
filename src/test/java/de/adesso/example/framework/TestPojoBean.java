package de.adesso.example.framework;

import java.util.List;

import de.adesso.example.framework.annotation.CallStrategy;
import de.adesso.example.framework.annotation.CallingStrategy;
import de.adesso.example.framework.annotation.Required;

public class TestPojoBean {

	public ApplicationProtocol<String> operation(final String aString, final ApplicationProtocol<String> state) {
		state.setResult(aString);
		return state;
	}

	public ApplicationProtocol<String> requiredParameterOperation(
			@Required final String aString,
			@Required final ApplicationProtocol<String> state) {
		state.setResult(aString);
		return state;
	}

	@CallStrategy(strategy = CallingStrategy.Eager)
	public ApplicationProtocol<String> listOperation(
			@Required(requireNotEmpty = true) final List<String> aList,
			@Required final ApplicationProtocol<String> state) {

		state.setResult(aList.get(0));
		return state;
	}

	public ApplicationProtocol<String> throwingOperation(
			@Required final String aString,
			@Required final ApplicationProtocol<String> state) throws Exception {
		throw new Exception("test exception");
	}

	@CallStrategy(strategy = CallingStrategy.Eager)
	public ApplicationProtocol<String> operationOtherType(
			@Required final String aString,
			@Required final ApplicationProtocol<String> state) {
		state.setResult(aString);

		return state;
	}

	@CallStrategy(strategy = CallingStrategy.Eager)
	public ApplicationProtocol<String> operationOtherType_2(
			@Required(requireNotEmpty = true) final List<String> aList,
			@Required final ApplicationProtocol<String> state) {
		state.setResult(aList.get(0));

		return state;
	}
}
