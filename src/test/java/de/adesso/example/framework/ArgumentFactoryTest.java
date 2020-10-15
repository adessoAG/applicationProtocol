package de.adesso.example.framework;

import static org.assertj.core.api.Assertions.assertThat;

import java.lang.reflect.Method;
import java.math.BigDecimal;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ArgumentFactoryTest {

	@Autowired
	private ArgumentFactory factory;

	@Test
	public void testCreateArgumentByName() throws NoSuchMethodException, SecurityException {
		final Method emulatedMethod = ToBeEmulated.class.getMethod("operation", String.class, int.class,
				ApplicationProtocol.class);
		final Method beanMethod = Bean.class.getMethod("doSomething", String.class);
		final Argument argument = this.factory.createArgumentByName(emulatedMethod, beanMethod, "aString");

		assertThat(argument)
				.isNotNull()
				.isInstanceOf(MethodArgument.class);
		final MethodArgument methodArgument = (MethodArgument) argument;
		assertThat(methodArgument.getSourcePosition())
				.isEqualTo(0);
		assertThat(methodArgument.getTargetPosition())
				.isEqualTo(0);
		assertThat(methodArgument.getType())
				.isEqualTo(String.class);
	}

	@Test(expected = AbigiousParameterException.class)
	public void testCreateArgumentByNameSourceOtherName() throws NoSuchMethodException, SecurityException {
		final Method emulatedMethod = ToBeEmulated_OtherParameterName.class.getMethod("operation", String.class,
				int.class,
				ApplicationProtocol.class);
		final Method beanMethod = Bean.class.getMethod("doSomething", String.class);
		this.factory.createArgumentByName(emulatedMethod, beanMethod, "aString");
	}

	@Test(expected = AbigiousParameterException.class)
	public void testCreateArgumentByNameTargetOtherName() throws NoSuchMethodException, SecurityException {
		final Method emulatedMethod = ToBeEmulated.class.getMethod("operation", String.class,
				int.class,
				ApplicationProtocol.class);
		final Method beanMethod = Bean_OtherParameterName.class.getMethod("doSomething", String.class);
		this.factory.createArgumentByName(emulatedMethod, beanMethod, "aString");
	}

	@Test
	public void testCreateArgumentByType() throws NoSuchMethodException, SecurityException {
		final Method emulatedMethod = ToBeEmulated.class.getMethod("operation", String.class, int.class,
				ApplicationProtocol.class);
		final Method beanMethod = Bean.class.getMethod("doSomething", String.class);
		final Argument argument = this.factory.createArgumentByType(emulatedMethod, beanMethod, String.class);

		assertThat(argument)
				.isNotNull()
				.isInstanceOf(MethodArgument.class);
		final MethodArgument methodArgument = (MethodArgument) argument;
		assertThat(methodArgument.getSourcePosition())
				.isEqualTo(0);
		assertThat(methodArgument.getTargetPosition())
				.isEqualTo(0);
		assertThat(methodArgument.getType())
				.isEqualTo(String.class);
	}

	@Test(expected = AbigiousParameterException.class)
	public void testCreateArgumentByTypeSourceOtherType() throws NoSuchMethodException, SecurityException {
		final Method emulatedMethod = ToBeEmulated_OtherParameterType.class.getMethod("operation", BigDecimal.class,
				int.class,
				ApplicationProtocol.class);
		final Method beanMethod = Bean.class.getMethod("doSomething", String.class);
		this.factory.createArgumentByType(emulatedMethod, beanMethod, String.class);
	}

	@Test(expected = AbigiousParameterException.class)
	public void testCreateArgumentByTypeTargetOtherType() throws NoSuchMethodException, SecurityException {
		final Method emulatedMethod = ToBeEmulated.class.getMethod("operation", String.class, int.class,
				ApplicationProtocol.class);
		final Method beanMethod = Bean_OtherParameterType.class.getMethod("doSomething", BigDecimal.class);
		this.factory.createArgumentByType(emulatedMethod, beanMethod, String.class);
	}

	private interface ToBeEmulated {

		ApplicationProtocol<String> operation(String aString, int aInt, ApplicationProtocol<String> state);
	}

	private interface ToBeEmulated_OtherParameterName {

		ApplicationProtocol<String> operation(String otherString, int otherInt, ApplicationProtocol<String> otherState);
	}

	private interface ToBeEmulated_OtherParameterType {

		ApplicationProtocol<String> operation(BigDecimal aString, int aInt, ApplicationProtocol<String> state);
	}

	private static class Bean {

		@SuppressWarnings("unused")
		public ApplicationProtocol<String> doSomething(final String aString) {
			final ApplicationProtocol<String> result = new ApplicationProtocol<>();
			result.setResult(aString);

			return result;
		}
	}

	private static class Bean_OtherParameterName {

		@SuppressWarnings("unused")
		public ApplicationProtocol<String> doSomething(final String otherString) {
			final ApplicationProtocol<String> result = new ApplicationProtocol<>();
			result.setResult(otherString);

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
}
