package de.adesso.example.framework;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.Assert;


@RunWith(SpringRunner.class)
public class FunctionSignatureArgumentTest {

	@Test
	public void testFunctionSignatureArgument() {
		final FunctionSignatureArgument argumentProcessor = new FunctionSignatureArgument(String.class, 2);
		Assert.notNull(argumentProcessor, "constructor has to create an object");
	}

	@Test
	public void testPrepareArgument() {
		final FunctionSignatureArgument argumentProcessor = new FunctionSignatureArgument(String.class, 2);
		final ApplicationProtocol<String> state = new ApplicationProtocol<>();
		final String testString = "Der Hase hüpft um die Ecke";
		final Object[] testArgs = { null, null, testString};
		final Object result = argumentProcessor.prepareArgument(state, testArgs);

		assertThat(result)
		.isInstanceOf(String.class)
		.isEqualTo(testString);
	}
}
