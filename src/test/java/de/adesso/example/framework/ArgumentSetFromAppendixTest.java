package de.adesso.example.framework;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.fail;

import java.math.BigDecimal;
import java.util.Set;
import java.util.UUID;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
public class ArgumentSetFromAppendixTest {

	@Test
	public void testConstructor() {
		final UUID attachmentId = UUID.randomUUID();

		final ArgumentSetFromAppendix result = new ArgumentSetFromAppendix(String.class, attachmentId);

		assertThat(result).isNotNull();
	}

	@Test(expected = NullPointerException.class)
	public void testConstructorTypeIsNull() {
		final UUID attachmentId = UUID.randomUUID();

		new ArgumentSetFromAppendix(null, attachmentId);

		fail("should detect null value for type");
	}

	@Test(expected = NullPointerException.class)
	public void testConstructorAttachmentIsNull() {
		new ArgumentSetFromAppendix(String.class, null);

		fail("should detect null value for attachmentId");
	}

	@Test
	public void testPrepareArgumentToSet() {
		final ApplicationProtocol<BigDecimal> state = new ApplicationProtocol<BigDecimal>()
				.addAppendix(new OtherAppendix("some string"))
				.addAppendix(new OtherAppendix("the last string"));
		final Object[] args = { "einfacher Teststring", Integer.valueOf(5), state };
		final ArgumentSetFromAppendix argumentProcessor = new ArgumentSetFromAppendix(String.class,
				TestAppendix.testAppendixId);

		@SuppressWarnings("unchecked") final Set<TestAppendix> result = (Set<TestAppendix>) argumentProcessor
				.prepareArgument(state, args);

		assertThat(result)
				.isNotNull()
				.hasOnlyElementsOfType(TestAppendix.class)
				.hasSize(2);
	}

	@Test
	public void testPrepareArgumentToSetEmpty() {
		final UUID otherId = UUID.randomUUID();
		final ApplicationProtocol<BigDecimal> state = new ApplicationProtocol<BigDecimal>()
				.addAppendix(new OtherAppendix("some string"))
				.addAppendix(new OtherAppendix("the last string"));
		final Object[] args = { "einfacher Teststring", Integer.valueOf(5), state };
		final ArgumentSetFromAppendix argumentProcessor = new ArgumentSetFromAppendix(String.class, otherId);

		@SuppressWarnings("unchecked") final Set<TestAppendix> result = (Set<TestAppendix>) argumentProcessor
				.prepareArgument(state, args);

		assertThat(result)
				.isNotNull()
				.hasSize(0);
	}

	@Test
	public void testPrepareArgumentToSetDifferentAppendixes() {
		final ApplicationProtocol<BigDecimal> state = new ApplicationProtocol<BigDecimal>()
				.addAppendix(new OtherAppendix("some string"))
				.addAppendix(new TestAppendix("another string"))
				.addAppendix(new TestAppendix("thrid string"))
				.addAppendix(new OtherAppendix("the last string"));
		final Object[] args = { "einfacher Teststring", Integer.valueOf(5), state };
		final ArgumentSetFromAppendix argumentProcessor = new ArgumentSetFromAppendix(String.class,
				TestAppendix.testAppendixId);

		@SuppressWarnings("unchecked") final Set<TestAppendix> result = (Set<TestAppendix>) argumentProcessor
				.prepareArgument(state, args);

		assertThat(result)
				.isNotNull()
				.hasOnlyElementsOfType(TestAppendix.class)
				.hasSize(2);
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

	private static class OtherAppendix extends ApplicationAppendix<String> {

		public static final UUID ownerId = UUID.randomUUID();
		public static final UUID testAppendixId = UUID.randomUUID();

		public OtherAppendix(final String content) {
			super(content);
		}

		@Override
		public UUID getOwner() {
			return OtherAppendix.ownerId;
		}

		@Override
		public UUID getAppendixId() {
			return OtherAppendix.testAppendixId;
		}
	}
}
