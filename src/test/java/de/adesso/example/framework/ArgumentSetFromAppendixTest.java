package de.adesso.example.framework;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.fail;

import java.math.BigDecimal;
import java.util.Set;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
public class ArgumentSetFromAppendixTest {

	@Test
	public void testConstructor() {
		final ArgumentSetFromAppendix result = new ArgumentSetFromAppendix(String.class,
				StringTestAppendix.class);

		assertThat(result).isNotNull();
	}

	@Test(expected = IllegalArgumentException.class)
	public void testConstructorTypeIsNull() {

		new ArgumentSetFromAppendix(null, StringTestAppendix.class);

		fail("should detect null value for type");
	}

	@Test(expected = IllegalArgumentException.class)
	public void testConstructorAttachmentIsNull() {
		new ArgumentSetFromAppendix(String.class, null);

		fail("should detect null value for attachmentId");
	}

	@Test
	public void testPrepareArgumentToSet() {
		final ApplicationProtocol<BigDecimal> state = new ApplicationProtocol<BigDecimal>()
				.addAppendix(new OtherStringTestAppendix("some string"))
				.addAppendix(new OtherStringTestAppendix("the last string"));
		final Object[] args = { "einfacher Teststring", Integer.valueOf(5), state };
		final ArgumentSetFromAppendix argumentProcessor = new ArgumentSetFromAppendix(String.class,
				OtherStringTestAppendix.class);

		@SuppressWarnings("unchecked")
		final Set<StringTestAppendix> result = (Set<StringTestAppendix>) argumentProcessor
				.prepareArgument(state, args);

		assertThat(result)
				.isNotNull()
				.hasOnlyElementsOfType(OtherStringTestAppendix.class)
				.hasSize(2);
	}

	@Test
	public void testPrepareArgumentToSetEmpty() {
		final ApplicationProtocol<BigDecimal> state = new ApplicationProtocol<BigDecimal>()
				.addAppendix(new OtherStringTestAppendix("some string"))
				.addAppendix(new OtherStringTestAppendix("the last string"));
		final Object[] args = { "einfacher Teststring", Integer.valueOf(5), state };
		final ArgumentSetFromAppendix argumentProcessor = new ArgumentSetFromAppendix(String.class,
				OtherStringTestAppendix.class);

		@SuppressWarnings("unchecked")
		final Set<StringTestAppendix> result = (Set<StringTestAppendix>) argumentProcessor
				.prepareArgument(state, args);

		assertThat(result)
				.isNotNull()
				.hasSize(0);
	}

	@Test
	public void testPrepareArgumentToSetDifferentAppendixes() {
		final ApplicationProtocol<BigDecimal> state = new ApplicationProtocol<BigDecimal>()
				.addAppendix(new OtherStringTestAppendix("some string"))
				.addAppendix(new StringTestAppendix("another string"))
				.addAppendix(new StringTestAppendix("thrid string"))
				.addAppendix(new OtherStringTestAppendix("the last string"));
		final Object[] args = { "einfacher Teststring", Integer.valueOf(5), state };
		final ArgumentSetFromAppendix argumentProcessor = new ArgumentSetFromAppendix(String.class,
				StringTestAppendix.class);

		@SuppressWarnings("unchecked")
		final Set<StringTestAppendix> result = (Set<StringTestAppendix>) argumentProcessor
				.prepareArgument(state, args);

		assertThat(result)
				.isNotNull()
				.hasOnlyElementsOfType(StringTestAppendix.class)
				.hasSize(2);
	}
}
