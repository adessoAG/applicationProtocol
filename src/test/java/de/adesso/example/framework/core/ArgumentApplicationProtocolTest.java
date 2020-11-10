package de.adesso.example.framework.core;

import static org.assertj.core.api.Assertions.assertThat;

import java.lang.reflect.Method;

import org.junit.jupiter.api.Test;

import de.adesso.example.framework.ApplicationProtocol;
import de.adesso.example.framework.annotation.Required;

class ArgumentApplicationProtocolTest {

	private final String testString = "test";

	@Test
	void testPrepareArgument() throws NoSuchMethodException, SecurityException {
		final ArgumentApplicationProtocol argument = new ArgumentApplicationProtocol();
		final Bean b = new Bean();
		final Method method = Bean.class.getMethod("doSomething", ApplicationProtocol.class);
		final BeanOperation beanOperation = BeanOperation.builder()
				.implementation(b)
				.argument(argument)
				.method(method)
				.methodIdentifier("doSomething")
				.build();
		argument.init(beanOperation, method.getParameters()[0], 1);
		final ApplicationProtocol<String> state = new ApplicationProtocol<>();
		final Object[] args = { state };

		final Object result = argument.prepareArgument(state, args);

		assertThat(result).isInstanceOf(ApplicationProtocol.class);
		assertThat(result).isEqualTo(state);
	}

	public class Bean {

		public ApplicationProtocol<String> doSomething(@Required final ApplicationProtocol<String> state) {
			state.setResult(ArgumentApplicationProtocolTest.this.testString);
			return state;
		}
	}
}
