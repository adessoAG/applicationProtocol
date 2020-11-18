package de.adesso.example.framework.core;

import static org.assertj.core.api.Assertions.assertThat;

import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import de.adesso.example.framework.ApplicationAppendix;
import de.adesso.example.framework.ApplicationProtocol;
import de.adesso.example.framework.StringTestAppendix;
import de.adesso.example.framework.exception.AppendixNotRegisteredException;

@RunWith(SpringRunner.class)
public class ArgumentFactoryTest {

	private ArgumentFactory factory;

	@Before
	public void setup() {
		final List<Class<? extends ApplicationAppendix<?>>> appendixClasses = new ArrayList<>();
		appendixClasses.add(StringTestAppendix.class);
		final AppendixRegistry appendixRegistry = new AppendixRegistryImpl(appendixClasses);
		this.factory = new ArgumentFactory(appendixRegistry);
	}

	@Test
	public void testCreateArgumentUniqueType() throws NoSuchMethodException, SecurityException {
		final Method emulatedMethod = ToBeEmulated.class.getMethod("operation", String.class, int.class,
				ApplicationProtocol.class);
		final Method beanMethod = Bean.class.getMethod("doSomething", String.class);
		final List<ParameterPosition> parameterList = ParameterPosition.buildParameterList(beanMethod);
		final Argument argument = this.factory.createArgument(emulatedMethod, beanMethod, parameterList.get(0));

		assertThat(argument)
				.isNotNull()
				.isInstanceOf(ArgumentFromMethod.class);
		final ArgumentFromMethod methodArgument = (ArgumentFromMethod) argument;
		assertThat(methodArgument.getSourcePosition())
				.isEqualTo(0);
		assertThat(methodArgument.getTargetPosition())
				.isEqualTo(0);
		assertThat(methodArgument.getType())
				.isEqualTo(String.class);
	}

	@Test
	public void testCreateArgumentNotUniqueTypeUniqueName() throws NoSuchMethodException, SecurityException {
		final Method emulatedMethod = ToBeEmulated_TwoString.class.getMethod("operation",
				String.class, String.class, ApplicationProtocol.class);
		final Method beanMethod = Bean.class.getMethod("doSomething", String.class);
		final List<ParameterPosition> parameterList = ParameterPosition.buildParameterList(beanMethod);
		final Argument argument = this.factory.createArgument(emulatedMethod, beanMethod, parameterList.get(0));

		assertThat(argument)
				.isNotNull()
				.isInstanceOf(ArgumentFromMethod.class);
		final ArgumentFromMethod methodArgument = (ArgumentFromMethod) argument;
		assertThat(methodArgument.getSourcePosition())
				.isEqualTo(1);
		assertThat(methodArgument.getTargetPosition())
				.isEqualTo(0);
		assertThat(methodArgument.getType())
				.isEqualTo(String.class);
	}

	@Test
	public void testCreateArgumentNotUniqueTypeNotUniqueName() throws NoSuchMethodException, SecurityException {
		final Method emulatedMethod = ToBeEmulated_OtherType.class.getMethod("operation", UUID.class,
				ApplicationProtocol.class);
		final Method beanMethod = Bean.class.getMethod("doSomething", String.class);
		final List<ParameterPosition> parameterList = ParameterPosition.buildParameterList(beanMethod);
		final Argument argument = this.factory.createArgument(emulatedMethod, beanMethod, parameterList.get(0));

		assertThat(argument)
				.isNotNull()
				.isInstanceOf(ArgumentFromAppendix.class);
		final ArgumentFromAppendix methodArgument = (ArgumentFromAppendix) argument;
		assertThat(methodArgument.getTargetPosition())
				.isEqualTo(0);
		assertThat(methodArgument.getType())
				.isEqualTo(String.class);
	}

	@Test
	public void testCreateArgumentByType() throws NoSuchMethodException, SecurityException {
		final Method emulatedMethod = ToBeEmulated.class.getMethod("operation", String.class, int.class,
				ApplicationProtocol.class);
		final Method beanMethod = Bean.class.getMethod("doSomething", String.class);
		final List<ParameterPosition> parameterList = ParameterPosition.buildParameterList(beanMethod);
		final Argument argument = this.factory.createArgument(emulatedMethod, beanMethod, parameterList.get(0));

		assertThat(argument)
				.isNotNull()
				.isInstanceOf(ArgumentFromMethod.class);
		final ArgumentFromMethod methodArgument = (ArgumentFromMethod) argument;
		assertThat(methodArgument.getSourcePosition())
				.isEqualTo(0);
		assertThat(methodArgument.getTargetPosition())
				.isEqualTo(0);
		assertThat(methodArgument.getType())
				.isEqualTo(String.class);
	}

	@Test(expected = AppendixNotRegisteredException.class)
	public void testCreateArgumentNoMatch() throws NoSuchMethodException, SecurityException {
		final Method emulatedMethod = ToBeEmulated_OtherType.class.getMethod("operation", UUID.class,
				ApplicationProtocol.class);
		final Method beanMethod = Bean_OtherParameterType.class.getMethod("doSomething", BigDecimal.class);
		final List<ParameterPosition> parameterList = ParameterPosition.buildParameterList(beanMethod);
		this.factory.createArgument(emulatedMethod, beanMethod, parameterList.get(0));
	}

	@Test
	public void testCreateArgumentList() throws NoSuchMethodException, SecurityException {
		final Method emulatedMethod = ToBeEmulated_OtherType.class.getMethod("operation", UUID.class,
				ApplicationProtocol.class);
		final Method beanMethod = Bean_List.class.getMethod("doSomething", List.class);
		final List<ParameterPosition> parameterList = ParameterPosition.buildParameterList(beanMethod);
		final Argument argument = this.factory.createArgument(emulatedMethod, beanMethod, parameterList.get(0));

		assertThat(argument)
				.isNotNull()
				.isInstanceOf(ArgumentListFromAppendix.class);
		final ArgumentListFromAppendix listArgument = (ArgumentListFromAppendix) argument;
		assertThat(listArgument.getTargetPosition())
				.isEqualTo(0);
		assertThat(listArgument.getType())
				.isEqualTo(String.class);
	}

	@Test
	public void testCreateArgumentSet() throws NoSuchMethodException, SecurityException {
		final Method emulatedMethod = ToBeEmulated_OtherType.class.getMethod("operation", UUID.class,
				ApplicationProtocol.class);
		final Method beanMethod = Bean_Set.class.getMethod("doSomething", Set.class);
		final List<ParameterPosition> parameterList = ParameterPosition.buildParameterList(beanMethod);
		final Argument argument = this.factory.createArgument(emulatedMethod, beanMethod, parameterList.get(0));

		assertThat(argument)
				.isNotNull()
				.isInstanceOf(ArgumentSetFromAppendix.class);
		final ArgumentSetFromAppendix setArgument = (ArgumentSetFromAppendix) argument;
		assertThat(setArgument.getTargetPosition())
				.isEqualTo(0);
		assertThat(setArgument.getType())
				.isEqualTo(String.class);
	}

	private interface ToBeEmulated {

		ApplicationProtocol<String> operation(String aString, int aInt, ApplicationProtocol<String> state);
	}

	private interface ToBeEmulated_OtherType {

		ApplicationProtocol<String> operation(UUID aUUID, ApplicationProtocol<String> state);
	}

	private interface ToBeEmulated_TwoString {

		ApplicationProtocol<String> operation(String otherString, final String aString,
				ApplicationProtocol<String> otherState);
	}

	private static class Bean {

		@SuppressWarnings("unused")
		public ApplicationProtocol<String> doSomething(final String aString) {
			final ApplicationProtocol<String> result = new ApplicationProtocol<>();
			result.setResult(aString);

			return result;
		}
	}

	private static class Bean_OtherParameterType {

		@SuppressWarnings("unused")
		public ApplicationProtocol<String> doSomething(final BigDecimal aString) {
			final ApplicationProtocol<String> result = new ApplicationProtocol<>();
			result.setResult("blubber");

			return result;
		}
	}

	private static class Bean_List {

		@SuppressWarnings("unused")
		public ApplicationProtocol<String> doSomething(final List<String> aStringList) {
			final ApplicationProtocol<String> result = new ApplicationProtocol<>();
			result.setResult("blubber");

			return result;
		}
	}

	private static class Bean_Set {

		@SuppressWarnings("unused")
		public ApplicationProtocol<String> doSomething(final Set<String> aStringList) {
			final ApplicationProtocol<String> result = new ApplicationProtocol<>();
			result.setResult("blubber");

			return result;
		}
	}
}
