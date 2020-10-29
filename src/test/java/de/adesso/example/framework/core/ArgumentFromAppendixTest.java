package de.adesso.example.framework.core;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.fail;

import java.math.BigDecimal;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import de.adesso.example.framework.ApplicationProtocol;
import de.adesso.example.framework.IntegerTestAppendix;
import de.adesso.example.framework.StringTestAppendix;
import de.adesso.example.framework.exception.TooManyElementsException;

@RunWith(SpringRunner.class)
public class ArgumentFromAppendixTest {

	@Test
	public void testConstructor() {
		final ArgumentFromAppendix appendix = new ArgumentFromAppendix(String.class, StringTestAppendix.class);

		assertThat(appendix).isNotNull();
	}

	@Test(expected = IllegalArgumentException.class)
	public void testConstructorTypeIsNull() {
		new ArgumentFromAppendix(null, StringTestAppendix.class);

		fail("should detect null value for type");
	}

	@Test(expected = IllegalArgumentException.class)
	public void testConstructorAttachmentIsNull() {
		new ArgumentFromAppendix(String.class, null);

		fail("should detect null value for attachmentId");
	}

	@Test
	public void testPrepareArgument() {
		final String testString = "some string";
		final StringTestAppendix additionalAppendix = new StringTestAppendix(testString);
		final ApplicationProtocol<BigDecimal> state = new ApplicationProtocol<BigDecimal>()
				.addAppendix(additionalAppendix);
		final Object[] args = { "einfacher Teststring", Integer.valueOf(5),
				state };
		final Argument argumentProcessor = new ArgumentFromAppendix(String.class,
				StringTestAppendix.class);

		final String result = (String) argumentProcessor.prepareArgument(state, args);

		assertThat(result)
				.isSameAs(testString);
	}

	@Test
	public void testPrepareArgumentMissingArgument() {
		final StringTestAppendix additionalAppendix = new StringTestAppendix("some string");
		final ApplicationProtocol<BigDecimal> state = new ApplicationProtocol<BigDecimal>()
				.addAppendix(additionalAppendix);
		final Object[] args = { "einfacher Teststring", Integer.valueOf(5),
				state };
		final Argument argumentProcessor = new ArgumentFromAppendix(String.class,
				IntegerTestAppendix.class);

		final String result = (String) argumentProcessor.prepareArgument(state, args);

		assertThat(result)
				.isNull();
	}

	@Test(expected = TooManyElementsException.class)
	public void testPrepareArgumentTooManyArguments() {
		final ApplicationProtocol<BigDecimal> state = new ApplicationProtocol<BigDecimal>()
				.addAppendix(new StringTestAppendix("some string"))
				.addAppendix(new StringTestAppendix("other string"));
		final Object[] args = { "einfacher Teststring", Integer.valueOf(5),
				state };
		final Argument argumentProcessor = new ArgumentFromAppendix(String.class,
				StringTestAppendix.class);

		argumentProcessor.prepareArgument(state, args);

		fail("should detect, that there are too many attachments of corresponding id available");
	}

	@Test
	public void testPrepareArgumentNoMatches() {
		final ApplicationProtocol<BigDecimal> state = new ApplicationProtocol<BigDecimal>()
				.addAppendix(new StringTestAppendix("some string"))
				.addAppendix(new StringTestAppendix("other string"));
		final Object[] args = { "einfacher Teststring", Integer.valueOf(5),
				state };
		final Argument argumentProcessor = new ArgumentFromAppendix(String.class,
				IntegerTestAppendix.class);

		final String argument = (String) argumentProcessor.prepareArgument(state, args);

		assertThat(argument)
				.isNull();
	}
}
