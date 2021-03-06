package de.adesso.example.framework.core;

import static org.assertj.core.api.Assertions.assertThat;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import de.adesso.example.framework.ApplicationProtocol;
import de.adesso.example.framework.TestConfig;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = { TestConfig.class })
public class ArgumentFactoryTest {

	@Autowired
	private ArgumentFactory factory;

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
				.isZero();
		assertThat(methodArgument.getTargetPosition())
				.isZero();
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
				.isZero();
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
				.isZero();
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
				.isZero();
		assertThat(methodArgument.getTargetPosition())
				.isZero();
		assertThat(methodArgument.getType())
				.isEqualTo(String.class);
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
				.isZero();
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
				.isZero();
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
