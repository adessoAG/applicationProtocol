package de.adesso.example.framework;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.Assert;


@RunWith(SpringRunner.class)
public class FunctionSignatureArgumentTest {

	@Test
	public void testConstructor() {
		final FunctionSignatureArgument argumentProcessor = new FunctionSignatureArgument(String.class, 2);
		Assert.notNull(argumentProcessor, "constructor has to create an object");
	}


	@Test
	public void testConstructorPosition0() {
		final FunctionSignatureArgument argumentProcessor = new FunctionSignatureArgument(String.class, 0);
		Assert.notNull(argumentProcessor, "constructor has to create an object");
	}

	@Test(expected = IndexOutOfBoundsException.class)
	public void testConstructorPositionNegative() {
		new FunctionSignatureArgument(String.class, -1);
		fail("position argument may not be negative");
	}

	@Test(expected = NullPointerException.class)
	public void testConstructorTypeNull() {
		new FunctionSignatureArgument(null, 0);
		fail("type argument may not be null");
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

	@Test(expected = IndexOutOfBoundsException.class)
	public void testPrepareArgumentPositionOutOfRange() {
		final FunctionSignatureArgument argumentProcessor = new FunctionSignatureArgument(String.class, 2);
		final ApplicationProtocol<String> state = new ApplicationProtocol<>();
		final String testString = "Der Hase hüpft um die Ecke";
		final Object[] testArgs = { null, testString};
		argumentProcessor.prepareArgument(state, testArgs);
		fail("should detect access of argument out of range");
	}
}
