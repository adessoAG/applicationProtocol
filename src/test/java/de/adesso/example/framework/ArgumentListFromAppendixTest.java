package de.adesso.example.framework;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.fail;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;


@RunWith(SpringRunner.class)
public class ArgumentListFromAppendixTest {

	@Test
	public void testConstructor() {
		final UUID attachmentId = UUID.randomUUID();

		final ArgumentListFromAppendix result = new ArgumentListFromAppendix(String.class, attachmentId);

		assertThat(result).isNotNull();
	}

	@Test(expected = NullPointerException.class)
	public void testConstructorTypeIsNull() {
		final UUID attachmentId = UUID.randomUUID();

		new ArgumentListFromAppendix(null, attachmentId);

		fail("should detect null value for type");
	}

	@Test(expected = NullPointerException.class)
	public void testConstructorAttachmentIsNull() {
		new ArgumentListFromAppendix(String.class, null);

		fail("should detect null value for attachmentId");
	}

	@Test
	public void testPrepareArgumentToList() {
		final UUID appendixId = UUID.randomUUID();
		final UUID owner = UUID.randomUUID();
		final ApplicationProtocol<BigDecimal> state = new ApplicationProtocol<>()
				.addAppendix(new TestAppendix(appendixId, owner))
				.addAppendix(new TestAppendix(appendixId, owner));
		final Object[]args = {"einfacher Teststring", Integer.valueOf(5), state};
		final ArgumentListFromAppendix argumentProcessor = new ArgumentListFromAppendix(String.class, appendixId);

		@SuppressWarnings("unchecked")
		final List<TestAppendix> result = (List<TestAppendix>) argumentProcessor.prepareArgument(state, args);

		assertThat(result)
		.isNotNull()
		.hasOnlyElementsOfType(TestAppendix.class)
		.hasSize(2);
	}

	@Test
	public void testPrepareArgumentToListEmpty() {
		final UUID appendixId = UUID.randomUUID();
		final UUID otherId = UUID.randomUUID();
		final UUID owner = UUID.randomUUID();
		final ApplicationProtocol<BigDecimal> state = new ApplicationProtocol<>()
				.addAppendix(new TestAppendix(appendixId, owner))
				.addAppendix(new TestAppendix(appendixId, owner));
		final Object[]args = {"einfacher Teststring", Integer.valueOf(5), state};
		final ArgumentListFromAppendix argumentProcessor = new ArgumentListFromAppendix(String.class, otherId);

		@SuppressWarnings("unchecked")
		final List<TestAppendix> result = (List<TestAppendix>) argumentProcessor.prepareArgument(state, args);

		assertThat(result)
		.isNotNull()
		.hasSize(0);
	}

	@Test
	public void testPrepareArgumentToListDifferentAppendixes() {
		final UUID appendixId = UUID.randomUUID();
		final UUID otherAppendixId = UUID.randomUUID();
		final UUID owner = UUID.randomUUID();
		final ApplicationProtocol<BigDecimal> state = new ApplicationProtocol<>()
				.addAppendix(new TestOtherAppendix(otherAppendixId, owner))
				.addAppendix(new TestAppendix(appendixId, owner))
				.addAppendix(new TestAppendix(appendixId, owner))
				.addAppendix(new TestOtherAppendix(otherAppendixId, owner));
		final Object[]args = {"einfacher Teststring", Integer.valueOf(5), state};
		final ArgumentListFromAppendix argumentProcessor = new ArgumentListFromAppendix(String.class, appendixId);

		@SuppressWarnings("unchecked")
		final List<TestAppendix> result = (List<TestAppendix>) argumentProcessor.prepareArgument(state, args);

		assertThat(result)
		.isNotNull()
		.hasOnlyElementsOfType(TestAppendix.class)
		.hasSize(2);
	}

	private class TestAppendix extends ApplicationAppendix {

		public TestAppendix(final UUID applicationAppendixId, final UUID owner) {
			super(applicationAppendixId, owner);
		}
	}

	private class TestOtherAppendix extends ApplicationAppendix {

		public TestOtherAppendix(final UUID applicationAppendixId, final UUID owner) {
			super(applicationAppendixId, owner);
		}
	}
}
