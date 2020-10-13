package de.adesso.example.framework;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.fail;

import java.math.BigDecimal;
import java.util.UUID;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

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
		final UUID appendixId = UUID.randomUUID();
		final UUID owner = UUID.randomUUID();
		final ApplicationAppendix additionalAppendix = new TestAppendix(appendixId, owner);
		final ApplicationProtocol<BigDecimal> state = new ApplicationProtocol<BigDecimal>()
				.addAppendix(additionalAppendix);
		final Object[] args = { "einfacher Teststring", Integer.valueOf(5), state };
		final ArgumentFromAppendix argumentProcessor = new ArgumentFromAppendix(String.class, appendixId);

		final TestAppendix result = (TestAppendix) argumentProcessor.prepareArgument(state, args);

		assertThat(result)
				.isNotNull()
				.isSameAs(additionalAppendix);
	}

	@Test
	public void testPrepareArgumentMissingArgument() {
		final UUID appendixId = UUID.randomUUID();
		final UUID owner = UUID.randomUUID();
		final UUID someOtherId = UUID.randomUUID();
		final ApplicationAppendix additionalAppendix = new TestAppendix(someOtherId, owner);
		final ApplicationProtocol<BigDecimal> state = new ApplicationProtocol<BigDecimal>()
				.addAppendix(additionalAppendix);
		final Object[] args = { "einfacher Teststring", Integer.valueOf(5), state };
		final ArgumentFromAppendix argumentProcessor = new ArgumentFromAppendix(String.class, appendixId);

		final TestAppendix result = (TestAppendix) argumentProcessor.prepareArgument(state, args);

		assertThat(result)
				.isNull();
	}

	@Test(expected = TooManyElementsException.class)
	public void testPrepareArgumentTooManyArguments() {
		final UUID appendixId = UUID.randomUUID();
		final UUID owner = UUID.randomUUID();
		final ApplicationProtocol<BigDecimal> state = new ApplicationProtocol<BigDecimal>()
				.addAppendix(new TestAppendix(appendixId, owner))
				.addAppendix(new TestAppendix(appendixId, owner));
		final Object[] args = { "einfacher Teststring", Integer.valueOf(5), state };
		final ArgumentFromAppendix argumentProcessor = new ArgumentFromAppendix(String.class, appendixId);

		argumentProcessor.prepareArgument(state, args);

		fail("should detect, that there are too many attachments of corresponding id available");
	}

	@Test
	public void testPrepareArgumentNoMatches() {
		final UUID appendixId = UUID.randomUUID();
		final UUID owner = UUID.randomUUID();
		final UUID someOtherId = UUID.randomUUID();
		final ApplicationProtocol<BigDecimal> state = new ApplicationProtocol<BigDecimal>()
				.addAppendix(new TestAppendix(someOtherId, owner))
				.addAppendix(new TestAppendix(someOtherId, owner));
		final Object[] args = { "einfacher Teststring", Integer.valueOf(5), state };
		final ArgumentFromAppendix argumentProcessor = new ArgumentFromAppendix(String.class, appendixId);

		final Object argument = argumentProcessor.prepareArgument(state, args);

		assertThat(argument)
				.isNull();
	}

	private class TestAppendix extends ApplicationAppendix {

		public TestAppendix(final UUID applicationAppendixId, final UUID owner) {
			super(applicationAppendixId, owner);
		}
	}
}
