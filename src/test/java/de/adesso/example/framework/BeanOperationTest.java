package de.adesso.example.framework;

import static org.assertj.core.api.Assertions.assertThat;

import java.lang.reflect.Method;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
public class BeanOperationTest {

	@Test
	public void testBuilder() throws NoSuchMethodException, SecurityException {
		final String methodIdentifier = "testMethod";
		final TestImplementation implementation = new TestImplementation();
		final Method declaredMethod = TestImplementation.class.getDeclaredMethod(methodIdentifier, String.class,
				int.class);

		final BeanOperation operation = BeanOperation.builder()
				.implementation(implementation)
				.methodIdentifier(methodIdentifier)
				.argument(new FunctionSignatureArgument(String.class, 0))
				.argument(new FunctionSignatureArgument(int.class, 1))
				.build();

		assertThat(operation)
				.isNotNull()
				.isInstanceOf(BeanOperation.class);
		assertThat(operation.getImplementation())
				.isNotNull()
				.isEqualTo(implementation);
		assertThat(operation.getMethod())
				.isNotNull()
				.isEqualTo(declaredMethod);
	}

	@Test(expected = NullPointerException.class)
	public void testBuilderMissingMethodIdentifier() throws NoSuchMethodException, SecurityException {
		final TestImplementation implementation = new TestImplementation();

		BeanOperation.builder()
				.implementation(implementation)
				.build();
	}

	@Test(expected = NullPointerException.class)
	public void testBuilderMissingImplementation() throws NoSuchMethodException, SecurityException {
		final String methodIdentifier = "testMethod";

		BeanOperation.builder()
				.methodIdentifier(methodIdentifier)
				.build();
	}

	@Test
	public void testGetArguments() throws NoSuchMethodException, SecurityException {
		final String methodIdentifier = "testMethod";
		final TestImplementation implementation = new TestImplementation();

		final BeanOperation operation = BeanOperation.builder()
				.implementation(implementation)
				.methodIdentifier(methodIdentifier)
				.argument(new FunctionSignatureArgument(String.class, 0))
				.argument(new FunctionSignatureArgument(int.class, 1))
				.build();

		assertThat(operation)
				.isNotNull()
				.isInstanceOf(BeanOperation.class);
		final List<Argument> arguments = operation.getArguments();
		assertThat(arguments)
				.isNotNull()
				.hasSize(2);
		assertThat(arguments.get(0))
				.isNotNull()
				.isInstanceOf(FunctionSignatureArgument.class);
		final Argument firstArgument = arguments.get(0);
		assertThat(firstArgument.getType())
				.isEqualTo(String.class);
		assertThat(arguments.get(1))
				.isNotNull()
				.isInstanceOf(FunctionSignatureArgument.class);
		final Argument secondArgument = arguments.get(1);
		assertThat(secondArgument.getType())
				.isEqualTo(int.class);
	}

	@Test
	public void testExecute() throws NoSuchMethodException, SecurityException {
		final String methodIdentifier = "testMethod";
		final TestImplementation implementation = new TestImplementation();

		final BeanOperation operation = BeanOperation.builder()
				.implementation(implementation)
				.methodIdentifier(methodIdentifier)
				.argument(new FunctionSignatureArgument(String.class, 0))
				.argument(new FunctionSignatureArgument(int.class, 1))
				.build();
		final String testString = "Das ist ein netter kleiner Teststring : ";
		final int testInt = 7;
		final Object[] args = { testString, testInt };
		final ApplicationProtocol<String> state = new ApplicationProtocol<>();
		final ApplicationProtocol<?> newState = operation.execute(state, args);

		assertThat(newState)
				.isNotNull();
		assertThat(newState.getResult())
				.isNotNull()
				.isInstanceOf(String.class)
				.isEqualTo(testString + testInt);
	}

	private class TestImplementation implements ApplicationFrameworkInvokable {

		@SuppressWarnings("unused")
		public ApplicationProtocol<String> testMethod(final String a, final int b) {
			final ApplicationProtocol<String> state = new ApplicationProtocol<>();
			state.setResult(a + b);

			return state;
		}
	}
}
