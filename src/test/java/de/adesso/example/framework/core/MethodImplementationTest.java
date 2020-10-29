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

import de.adesso.example.framework.ApplicationAppendix;
import de.adesso.example.framework.ApplicationOwner;
import de.adesso.example.framework.ApplicationProtocol;
import de.adesso.example.framework.EmulatedInterface;
import lombok.Getter;

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

		assertThat(resultState)
				.isNotNull();
		assertThat(resultState.getResult())
				.isNotNull()
				.isEqualTo(testString + anotherTestString);

		final Optional<ApplicationAppendix<?>> a1 = resultState.getAppendixOfClass(Appendix_A1.class);
		assertThat(a1)
				.isNotEmpty();
		final Appendix_A1 appendix_A1 = (Appendix_A1) a1.get();
		assertThat(appendix_A1.getContent())
				.isEqualTo(5);

		final Optional<ApplicationAppendix<?>> a2 = resultState.getAppendixOfClass(Appendix_A2.class);
		assertThat(a2)
				.isNotEmpty();
		assertThat(a2.get())
				.isInstanceOf(Appendix_A2.class);
		final Appendix_A2 appendix_A2 = (Appendix_A2) a2.get();
		assertThat(appendix_A2.getContent())
				.isEqualTo(anotherTestString);

		final List<ApplicationAppendix<?>> b2List = resultState.getAllAppenixesOfTypeAsList(Appendix_B2.class);
		assertThat(b2List)
				.isNotNull()
				.hasSize(2);
		final Appendix_B2 appendix_B2 = (Appendix_B2) b2List.get(0);
		assertThat(appendix_B2.getContent())
				.isEqualTo(anotherTestString);
	}

	private MethodImplementation createImplementation() throws NoSuchMethodException {
		final MethodImplementation implementation = MethodImplementation.builder()
				.methodIdentifier(EmulatedInterface.method_1)
				.returnValueType(String.class)
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

	@Getter
	private static class Appendix_A1 extends ApplicationAppendix<Integer> {

		public Appendix_A1(final Integer content) {
			super(content);
		}

		@Override
		public UUID getOwner() {
			return owner1.getOwnerId();
		}
	}

	private static class TestBean_1 {

		@SuppressWarnings("unused")
		public ApplicationProtocol<String> doSomething(final String aString, final int anInt, final Integer anInteger) {
			final ApplicationProtocol<String> state = new ApplicationProtocol<>();
			state.setResult(aString);
			state.addAppendix(new Appendix_A1(5));

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

	@Getter
	private static class Appendix_A2 extends ApplicationAppendix<String> {

		public Appendix_A2(final String content) {
			super(content);
		}

		@Override
		public UUID getOwner() {
			return owner2.getOwnerId();
		}
	}

	@Getter
	private static class Appendix_B2 extends ApplicationAppendix<String> {

		public Appendix_B2(final String content) {
			super(content);
		}

		@Override
		public UUID getOwner() {
			return owner2.getOwnerId();
		}
	}

	private class TestBean_2 {

		@SuppressWarnings("unused")
		public ApplicationProtocol<String> anotherAction(final String anotherString,
				final ApplicationProtocol<String> state) {
			state.setResult(state.getResult() + anotherString);
			state.addAppendix(new Appendix_A2(anotherString));
			state.addAppendix(new Appendix_B2(anotherString));
			state.addAppendix(new Appendix_B2(anotherString));

			return state;
		}
	}
}
