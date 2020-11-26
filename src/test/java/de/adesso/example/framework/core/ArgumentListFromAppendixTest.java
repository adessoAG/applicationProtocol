package de.adesso.example.framework.core;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.fail;

import java.math.BigDecimal;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import de.adesso.example.framework.ApplicationProtocol;
import de.adesso.example.framework.TestOwner;

@RunWith(SpringRunner.class)
public class ArgumentListFromAppendixTest {

	private final TestOwner owner = new TestOwner();

	@Test
	public void testConstructor() {
		final ArgumentListFromAppendix result = new ArgumentListFromAppendix(String.class);

		assertThat(result).isNotNull();
	}

	@Test(expected = IllegalArgumentException.class)
	public void testConstructorTypeIsNull() {
		new ArgumentListFromAppendix(null);

		fail("should detect null value for type");
	}

	@Test
	public void testPrepareArgumentToList() {
		final ApplicationProtocol<BigDecimal> state = new ApplicationProtocol<BigDecimal>()
				.addAppendix(this.owner, "some string")
				.addAppendix(this.owner, "other string");
		final Object[] args = { "einfacher Teststring", Integer.valueOf(5), state };
		final ArgumentListFromAppendix argumentProcessor = new ArgumentListFromAppendix(String.class);

		@SuppressWarnings("unchecked")
		final List<String> result = (List<String>) argumentProcessor.prepareArgument(state, args);

		assertThat(result)
				.isNotNull()
				.hasOnlyElementsOfType(String.class)
				.hasSize(2);
	}

	@Test
	public void testPrepareArgumentToListEmpty() {
		final ApplicationProtocol<BigDecimal> state = new ApplicationProtocol<BigDecimal>()
				.addAppendix(this.owner, "some string")
				.addAppendix(this.owner, "other string");
		final Object[] args = { "einfacher Teststring", Integer.valueOf(5), state };
		final ArgumentListFromAppendix argumentProcessor = new ArgumentListFromAppendix(Integer.class);

		@SuppressWarnings("unchecked")
		final List<Integer> result = (List<Integer>) argumentProcessor.prepareArgument(state, args);

		assertThat(result)
				.isNotNull()
				.hasSize(0);
	}
}
