package de.adesso.example.framework;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.UUID;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import lombok.Getter;

@RunWith(SpringRunner.class)
public class MethodImplementationTest {

	@Test
	public void testBuilder() throws NoSuchMethodException {
		final MethodImplementation implementation = createImplementation();

		assertThat(implementation)
				.isNotNull()
				.isInstanceOf(MethodImplementation.class);
	}

	@Test
	public void testGetMethodIdentifier() throws NoSuchMethodException {
		final MethodImplementation implementation = createImplementation();

		assertThat(implementation.getMethodIdentifier())
				.isEqualTo(EmulatedInterface.method_1);
	}

	@Test
	public void testExecute() throws NoSuchMethodException, SecurityException {
		final MethodImplementation implementation = createImplementation();

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

		final Object a1 = resultState.getAppendixOfType(Appendix_A1.APPENDIX_A1_ID);
		assertThat(a1)
				.isNotNull()
				.isInstanceOf(Appendix_A1.class);
		final Appendix_A1 appendix_A1 = (Appendix_A1) a1;
		assertThat(appendix_A1.getContent())
				.isEqualTo(5);

		final Object a2 = resultState.getAppendixOfType(Appendix_A2.APPENDIX_A2_ID);
		assertThat(a2)
				.isNotNull()
				.isInstanceOf(Appendix_A2.class);
		final Appendix_A2 appendix_A2 = (Appendix_A2) a2;
		assertThat(appendix_A2.getContent())
				.isEqualTo(anotherTestString);

		final List<ApplicationAppendix<?>> b2List = resultState.getAllAppenixesOfTypeAsList(Appendix_B2.APPENDIX_B2_ID);
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
						.argument(new MethodArgument(String.class, 0))
						.argument(new MethodArgument(int.class, 1))
						.argument(new MethodArgument(Integer.class, 2))
						.build())
				.beanOperation(BeanOperation.builder()
						.implementation(new TestBean_2())
						.methodIdentifier("anotherAction")
						.argument(new MethodArgument(String.class, 3))
						.argument(new ApplicationProtocolAsArgument())
						.build())
				.build();
		implementation
				.method(EmulatedInterface.class.getDeclaredMethod(EmulatedInterface.method_1, String.class,
						int.class,
						Integer.class, String.class));
		return implementation;
	}

	// ------------------------------------------------------------------------//

	private final static Owner_1 owner1 = new Owner_1();

	private static class Owner_1 extends ApplicationOwner {

		public static final UUID ownerId = UUID.randomUUID();

		@Override
		protected UUID getOwner() {
			return ownerId;
		}
	}

	@Getter
	private static class Appendix_A1 extends ApplicationAppendix<Integer> {

		private final static UUID APPENDIX_A1_ID = UUID.randomUUID();

		public Appendix_A1(final Integer content) {
			super(content);
		}

		@Override
		public UUID getOwner() {
			return owner1.getOwner();
		}

		@Override
		public UUID getAppendixId() {
			return APPENDIX_A1_ID;
		}
	}

	private static class TestBean_1 implements ApplicationFrameworkInvokable {

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
		protected UUID getOwner() {
			return ownerId;
		}
	}

	@Getter
	private static class Appendix_A2 extends ApplicationAppendix<String> {

		private final static UUID APPENDIX_A2_ID = UUID.randomUUID();

		public Appendix_A2(final String content) {
			super(content);
		}

		@Override
		public UUID getOwner() {
			return owner2.getOwner();
		}

		@Override
		public UUID getAppendixId() {
			return APPENDIX_A2_ID;
		}
	}

	@Getter
	private static class Appendix_B2 extends ApplicationAppendix<String> {

		private final static UUID APPENDIX_B2_ID = UUID.randomUUID();

		public Appendix_B2(final String content) {
			super(content);
		}

		@Override
		public UUID getOwner() {
			return owner2.getOwner();
		}

		@Override
		public UUID getAppendixId() {
			return APPENDIX_B2_ID;
		}
	}

	private class TestBean_2 implements ApplicationFrameworkInvokable {

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
