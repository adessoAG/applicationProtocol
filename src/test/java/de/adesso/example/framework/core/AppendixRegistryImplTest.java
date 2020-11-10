package de.adesso.example.framework.core;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.Test;

import de.adesso.example.framework.ApplicationAppendix;

class AppendixRegistryImplTest {

	@Test
	void testLookUpFound() {
		final List<Class<? extends ApplicationAppendix<?>>> appendixClasses = new ArrayList<>();
		appendixClasses.add(A1.class);
		final AppendixRegistryImpl impl = new AppendixRegistryImpl(this.getClass().getClassLoader(), appendixClasses);

		final Class<? extends ApplicationAppendix<?>> result = impl.lookUp(String.class);

		assertThat(result).isNotNull();
		assertThat(result).isEqualTo(A1.class);
	}

	@Test
	void testLookUpNotFound() {
		final List<Class<? extends ApplicationAppendix<?>>> appendixClasses = new ArrayList<>();
		appendixClasses.add(A1.class);
		final AppendixRegistryImpl impl = new AppendixRegistryImpl(this.getClass().getClassLoader(), appendixClasses);

		final Class<? extends ApplicationAppendix<?>> result = impl.lookUp(BigDecimal.class);

		assertThat(result).isNull();
	}

	private final static UUID ownerId = UUID.randomUUID();

	private class A1 extends ApplicationAppendix<String> {

		protected A1(final String content) {
			super(content);
		}

		@Override
		public UUID getOwner() {
			return ownerId;
		}

	}

}
