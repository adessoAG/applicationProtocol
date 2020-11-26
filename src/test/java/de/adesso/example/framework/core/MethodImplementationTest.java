package de.adesso.example.framework.core;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.junit4.SpringRunner;

import de.adesso.example.framework.ApplicationOwner;
import de.adesso.example.framework.ApplicationProtocol;
import de.adesso.example.framework.EmulatedInterface;
import lombok.AllArgsConstructor;

@RunWith(SpringRunner.class)
public class MethodImplementationTest {

	@Mock
	private ApplicationContext contextMock;
	@Mock
	private DaisyChainDispatcher dispatcherMock;

	@Test
	public void testBuilder() throws NoSuchMethodException {
		final MethodImplementation implementation = this.createImplementation();

		assertThat(implementation)
				.isNotNull()
				.isInstanceOf(MethodImplementation.class);
	}

	@Test
	public void testGetMethodIdentifier() throws NoSuchMethodException {
		final MethodImplementation implementation = this.createImplementation();

		assertThat(implementation.getMethodIdentifier())
				.isEqualTo(EmulatedInterface.method_1);
	}

	@Test
	public void testExecute() throws NoSuchMethodException, SecurityException {
		final MethodImplementation implementation = this.createImplementation();

		final ApplicationProtocol<String> state = new ApplicationProtocol<>();
		final String testString = "Ein einfacher Teststring";
		final int testInt = 5;
		final Integer testInteger = 17;
		final String anotherTestString = "Das ist der zweite String für einen Test";
		final Object[] args = { testString, testInt, testInteger, anotherTestString };
		final ApplicationProtocol<String> resultState = implementation.execute(state, args);

		assertThat(resultState.getResult())
				.isEqualTo(testString + anotherTestString);

		final Optional<A1> optionalA1 = resultState.getAppendixOfClassT(A1.class);
		assertThat(optionalA1)
				.isNotNull()
				.isNotEmpty();
		final A1 a1 = optionalA1.get();
		assertThat(a1)
				.isInstanceOf(A1.class);
		assertThat(a1.anInteger)
				.isEqualTo(testInteger + testInt);

		final Optional<B1> optionalB1 = resultState.getAppendixOfClassT(B1.class);
		assertThat(optionalB1)
				.isNotNull()
				.isNotEmpty();
		final B1 b1 = optionalB1.get();
		assertThat(b1)
				.isInstanceOf(B1.class);
		assertThat(b1.aString)
				.isEqualTo(anotherTestString);

		final List<B2> b2List = resultState.getAllAppenixesOfTypeAsListT(B2.class);
		assertThat(b2List)
				.isNotNull()
				.hasSize(2);
		final B2 appendix_B2 = b2List.get(0);
		assertThat(appendix_B2.aString)
				.isEqualTo(anotherTestString);
	}

	private MethodImplementation createImplementation() throws NoSuchMethodException {
		final MethodImplementation implementation = MethodImplementation.builder()
				.methodIdentifier(EmulatedInterface.method_1)
				.beanOperation(BeanOperation.builder()
						.implementation(new TestBean_1())
						.methodIdentifier("doSomething")
						.argument(new ArgumentFromMethod(String.class, 0))
						.argument(new ArgumentFromMethod(int.class, 1))
						.argument(new ArgumentFromMethod(Integer.class, 2))
						.build())
				.beanOperation(BeanOperation.builder()
						.implementation(new TestBean_2())
						.methodIdentifier("anotherAction")
						.argument(new ArgumentFromMethod(String.class, 3))
						.argument(new ArgumentApplicationProtocol())
						.build())
				.build();
		implementation
				.method(EmulatedInterface.class.getDeclaredMethod(EmulatedInterface.method_1, String.class,
						int.class,
						Integer.class, String.class));
		implementation.init(this.dispatcherMock, this.contextMock);
		return implementation;
	}

	// ------------------------------------------------------------------------//

	private final static Owner_1 owner1 = new Owner_1();

	private static class Owner_1 extends ApplicationOwner {

		public static final UUID ownerId = UUID.randomUUID();

		@Override
		protected UUID getOwnerId() {
			return ownerId;
		}
	}

	@AllArgsConstructor
	private static class A1 {

		Integer anInteger;
	}

	private static class TestBean_1 {

		@SuppressWarnings("unused")
		public ApplicationProtocol<String> doSomething(final String aString, final int anInt, final Integer anInteger) {
			final ApplicationProtocol<String> state = new ApplicationProtocol<>();
			state.setResult(aString);
			state.addAppendix(owner1, new A1(Integer.valueOf(anInt + anInteger.intValue())));

			return state;
		}
	}

	private final static Owner_2 owner2 = new Owner_2();

	private static class Owner_2 extends ApplicationOwner {

		public static final UUID ownerId = UUID.randomUUID();

		@Override
		protected UUID getOwnerId() {
			return ownerId;
		}
	}

	@AllArgsConstructor
	private static class B1 {

		String aString;
	}

	@AllArgsConstructor
	private static class B2 {

		String aString;
	}

	public class TestBean_2 {

		public ApplicationProtocol<String> anotherAction(final String anotherString,
				final ApplicationProtocol<String> state) {
			state.setResult(state.getResult() + anotherString);
			state.addAppendix(owner2, new B1(anotherString));
			state.addAppendix(owner2, new B2(anotherString));
			state.addAppendix(owner2, new B2(anotherString));

			return state;
		}
	}
}
