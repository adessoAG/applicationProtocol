package de.adesso.example.framework.core;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.fail;

import java.math.BigDecimal;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import de.adesso.example.framework.ApplicationProtocol;
import de.adesso.example.framework.TestOwner;
import de.adesso.example.framework.exception.TooManyElementsException;

@RunWith(SpringRunner.class)
public class ArgumentFromAppendixTest {

	private final TestOwner owner = new TestOwner();

	@Test
	public void testConstructor() {
		final ArgumentFromAppendix argument = new ArgumentFromAppendix(String.class);

		assertThat(argument).isNotNull();
	}

	@Test(expected = IllegalArgumentException.class)
	public void testConstructorAttachmentIsNull() {
		new ArgumentFromAppendix(null);

		fail("should detect null value for attachmentId");
	}

	@Test
	public void testPrepareArgument() {
		final String testString = "some string";
		final ApplicationProtocol<BigDecimal> state = new ApplicationProtocol<BigDecimal>()
				.addAppendix(this.owner, testString);
		final Object[] args = { "einfacher Teststring", Integer.valueOf(5), state };
		final Argument argumentProcessor = new ArgumentFromAppendix(String.class);

		final String result = (String) argumentProcessor.prepareArgument(state, args);

		assertThat(result)
				.isSameAs(testString);
	}

	@Test
	public void testPrepareArgumentMissingArgument() {
		final ApplicationProtocol<BigDecimal> state = new ApplicationProtocol<>();
		final Object[] args = { "einfacher Teststring", Integer.valueOf(5),
				state };
		final Argument argumentProcessor = new ArgumentFromAppendix(String.class);

		final String result = (String) argumentProcessor.prepareArgument(state, args);

		assertThat(result)
				.isNull();
	}

	@Test(expected = TooManyElementsException.class)
	public void testPrepareArgumentTooManyArguments() {
		final ApplicationProtocol<BigDecimal> state = new ApplicationProtocol<BigDecimal>()
				.addAppendix(this.owner, "some string")
				.addAppendix(this.owner, "other string");
		final Object[] args = { "einfacher Teststring", Integer.valueOf(5), state };
		final Argument argumentProcessor = new ArgumentFromAppendix(String.class);

		argumentProcessor.prepareArgument(state, args);

		fail("should detect, that there are too many attachments of corresponding id available");
	}
}
