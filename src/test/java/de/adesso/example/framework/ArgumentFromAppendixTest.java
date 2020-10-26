package de.adesso.example.framework;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.fail;

import java.math.BigDecimal;
import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

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
		final StringTestAppendix additionalAppendix = new StringTestAppendix("some string");
		final ApplicationProtocol<BigDecimal> state = new ApplicationProtocol<BigDecimal>()
				.addAppendix(additionalAppendix);
		final Object[] args = { "einfacher Teststring", Integer.valueOf(5),
				state };
		final ArgumentFromAppendix argumentProcessor = new ArgumentFromAppendix(String.class,
				StringTestAppendix.class);

		@SuppressWarnings("unchecked")
		final Optional<StringTestAppendix> result = (Optional<StringTestAppendix>) argumentProcessor
				.prepareArgument(state, args);

		assertThat(result)
				.isNotEmpty();
		assertThat(result.get())
				.isSameAs(additionalAppendix);
	}

	@Test
	public void testPrepareArgumentMissingArgument() {
		final StringTestAppendix additionalAppendix = new StringTestAppendix("some string");
		final ApplicationProtocol<BigDecimal> state = new ApplicationProtocol<BigDecimal>()
				.addAppendix(additionalAppendix);
		final Object[] args = { "einfacher Teststring", Integer.valueOf(5),
				state };
		final ArgumentFromAppendix argumentProcessor = new ArgumentFromAppendix(String.class,
				IntegerTestAppendix.class);

		@SuppressWarnings("unchecked")
		final Optional<StringTestAppendix> result = (Optional<StringTestAppendix>) argumentProcessor.prepareArgument(state,
				args);

		assertThat(result)
				.isEmpty();
	}

	@Test(expected = TooManyElementsException.class)
	public void testPrepareArgumentTooManyArguments() {
		final ApplicationProtocol<BigDecimal> state = new ApplicationProtocol<BigDecimal>()
				.addAppendix(new StringTestAppendix("some string"))
				.addAppendix(new StringTestAppendix("other string"));
		final Object[] args = { "einfacher Teststring", Integer.valueOf(5),
				state };
		final ArgumentFromAppendix argumentProcessor = new ArgumentFromAppendix(String.class,
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
		final ArgumentFromAppendix argumentProcessor = new ArgumentFromAppendix(String.class,
				IntegerTestAppendix.class);

		@SuppressWarnings("unchecked")
		final Optional<StringTestAppendix> argument = (Optional<StringTestAppendix>) argumentProcessor.prepareArgument(state,
				args);

		assertThat(argument)
				.isEmpty();
	}
}
