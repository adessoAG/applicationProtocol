package de.adesso.example.framework.core;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import de.adesso.example.framework.ApplicationProtocol;

@RunWith(SpringRunner.class)
public class ArgumentFromMethodTest {

	@Test
	public void testConstructor() {
		final Argument argumentProcessor = new ArgumentFromMethod(String.class, 2);
		assertThat(argumentProcessor).isNotNull();
	}

	@Test
	public void testConstructorPosition0() {
		final Argument argumentProcessor = new ArgumentFromMethod(String.class, 0);
		assertThat(argumentProcessor).isNotNull();
	}

	@Test(expected = IndexOutOfBoundsException.class)
	public void testConstructorPositionNegative() {
		new ArgumentFromMethod(String.class, -1);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testConstructorTypeNull() {
		new ArgumentFromMethod(null, 0);
	}

	@Test
	public void testPrepareArgument() {
		final Argument argumentProcessor = new ArgumentFromMethod(String.class, 2);
		final ApplicationProtocol<String> state = new ApplicationProtocol<>();
		final String testString = "Der Hase hüpft um die Ecke";
		final Object[] testArgs = { null, null, testString };

		final Object result = argumentProcessor.prepareArgument(state, testArgs);

		assertThat(result)
				.isInstanceOf(String.class)
				.isEqualTo(testString);
	}

	@Test(expected = IndexOutOfBoundsException.class)
	public void testPrepareArgumentPositionOutOfRange() {
		final Argument argumentProcessor = new ArgumentFromMethod(String.class, 2);
		final ApplicationProtocol<String> state = new ApplicationProtocol<>();
		final String testString = "Der Hase hüpft um die Ecke";
		final Object[] testArgs = { null, testString };
		argumentProcessor.prepareArgument(state, testArgs);
		fail("should detect access of argument out of range");
	}
}
