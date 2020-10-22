package de.adesso.example.framework;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.fail;

import java.math.BigDecimal;
import java.util.UUID;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import de.adesso.example.framework.exception.TooManyElementsException;

@RunWith(SpringRunner.class)
public class ArgumentFromAppendixTest {

	@Test
	public void testConstructor() {
		final UUID attachmentId = UUID.randomUUID();

		final ArgumentFromAppendix result = new ArgumentFromAppendix(String.class, attachmentId);

		assertThat(result).isNotNull();
	}

	@Test(expected = NullPointerException.class)
	public void testConstructorTypeIsNull() {
		final UUID attachmentId = UUID.randomUUID();

		new ArgumentFromAppendix(null, attachmentId);

		fail("should detect null value for type");
	}

	@Test(expected = NullPointerException.class)
	public void testConstructorAttachmentIsNull() {
		new ArgumentFromAppendix(String.class, null);

		fail("should detect null value for attachmentId");
	}

	@Test
	public void testPrepareArgument() {
		final TestAppendix additionalAppendix = new TestAppendix("some string");
		final ApplicationProtocol<BigDecimal> state = new ApplicationProtocol<BigDecimal>()
				.addAppendix(additionalAppendix);
		final Object[] args = { "einfacher Teststring", Integer.valueOf(5), state };
		final ArgumentFromAppendix argumentProcessor = new ArgumentFromAppendix(String.class,
				TestAppendix.testAppendixId);

		final TestAppendix result = (TestAppendix) argumentProcessor.prepareArgument(state, args);

		assertThat(result)
				.isNotNull()
				.isSameAs(additionalAppendix);
	}

	@Test
	public void testPrepareArgumentMissingArgument() {
		final UUID someOtherId = UUID.randomUUID();
		final TestAppendix additionalAppendix = new TestAppendix("some string");
		final ApplicationProtocol<BigDecimal> state = new ApplicationProtocol<BigDecimal>()
				.addAppendix(additionalAppendix);
		final Object[] args = { "einfacher Teststring", Integer.valueOf(5), state };
		final ArgumentFromAppendix argumentProcessor = new ArgumentFromAppendix(String.class, someOtherId);

		final TestAppendix result = (TestAppendix) argumentProcessor.prepareArgument(state, args);

		assertThat(result)
				.isNull();
	}

	@Test(expected = TooManyElementsException.class)
	public void testPrepareArgumentTooManyArguments() {
		final ApplicationProtocol<BigDecimal> state = new ApplicationProtocol<BigDecimal>()
				.addAppendix(new TestAppendix("some string"))
				.addAppendix(new TestAppendix("other string"));
		final Object[] args = { "einfacher Teststring", Integer.valueOf(5), state };
		final ArgumentFromAppendix argumentProcessor = new ArgumentFromAppendix(String.class,
				TestAppendix.testAppendixId);

		argumentProcessor.prepareArgument(state, args);

		fail("should detect, that there are too many attachments of corresponding id available");
	}

	@Test
	public void testPrepareArgumentNoMatches() {
		final UUID someOtherId = UUID.randomUUID();
		final ApplicationProtocol<BigDecimal> state = new ApplicationProtocol<BigDecimal>()
				.addAppendix(new TestAppendix("some string"))
				.addAppendix(new TestAppendix("other string"));
		final Object[] args = { "einfacher Teststring", Integer.valueOf(5), state };
		final ArgumentFromAppendix argumentProcessor = new ArgumentFromAppendix(String.class, someOtherId);

		final Object argument = argumentProcessor.prepareArgument(state, args);

		assertThat(argument)
				.isNull();
	}

	private static class TestAppendix extends ApplicationAppendix<String> {

		public static final UUID ownerId = UUID.randomUUID();
		public static final UUID testAppendixId = UUID.randomUUID();

		public TestAppendix(final String content) {
			super(content);
		}

		@Override
		public UUID getOwner() {
			return TestAppendix.ownerId;
		}

		@Override
		public UUID getAppendixId() {
			return TestAppendix.testAppendixId;
		}
	}
}
