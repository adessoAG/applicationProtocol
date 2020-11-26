package de.adesso.example.framework.core;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.fail;

import java.math.BigDecimal;
import java.util.Set;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import de.adesso.example.framework.ApplicationProtocol;
import de.adesso.example.framework.Other;
import de.adesso.example.framework.TestOwner;

@RunWith(SpringRunner.class)
public class ArgumentSetFromAppendixTest {

	private final TestOwner owner = new TestOwner();

	@Test
	public void testConstructor() {
		final ArgumentSetFromAppendix result = new ArgumentSetFromAppendix(String.class);

		assertThat(result).isNotNull();
	}

	@Test(expected = IllegalArgumentException.class)
	public void testConstructorTypeIsNull() {

		new ArgumentSetFromAppendix(null);

		fail("should detect null value for type");
	}

	@Test
	public void testPrepareArgumentToSet() {
		final ApplicationProtocol<BigDecimal> state = new ApplicationProtocol<BigDecimal>()
				.addAppendix(this.owner, new Other("some string", 5))
				.addAppendix(this.owner, new Other("the last string", 7));
		final Object[] args = { "einfacher Teststring", Integer.valueOf(5), state };
		final ArgumentSetFromAppendix argumentProcessor = new ArgumentSetFromAppendix(Other.class);

		@SuppressWarnings("unchecked")
		final Set<Other> result = (Set<Other>) argumentProcessor
				.prepareArgument(state, args);

		assertThat(result)
				.isNotNull()
				.hasOnlyElementsOfType(Other.class)
				.hasSize(2);
	}

	@Test
	public void testPrepareArgumentToSetEmpty() {
		final ApplicationProtocol<BigDecimal> state = new ApplicationProtocol<BigDecimal>()
				.addAppendix(this.owner, new Other("some string", 5))
				.addAppendix(this.owner, new Other("the last string", 7));
		final Object[] args = { "einfacher Teststring", Integer.valueOf(5), state };
		final ArgumentSetFromAppendix argumentProcessor = new ArgumentSetFromAppendix(Integer.class);

		@SuppressWarnings("unchecked")
		final Set<Integer> result = (Set<Integer>) argumentProcessor
				.prepareArgument(state, args);

		assertThat(result)
				.isNotNull()
				.hasSize(0);
	}
}
