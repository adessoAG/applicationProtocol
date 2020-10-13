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

	private final String emulatedMethodIdentifier = "operation";

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
				.isEqualTo(this.emulatedMethodIdentifier);
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
		assertThat(appendix_A1.getAnInt())
				.isEqualTo(testInt);
		assertThat(appendix_A1.getAnInteger())
				.isEqualTo(testInteger);

		final Object a2 = resultState.getAppendixOfType(Appendix_A2.APPENDIX_A2_ID);
		assertThat(a2)
				.isNotNull()
				.isInstanceOf(Appendix_A2.class);
		final Appendix_A2 appendix_A2 = (Appendix_A2) a2;
		assertThat(appendix_A2.getAString())
				.isEqualTo(anotherTestString);

		final List<Appendix_B2> b2List = resultState.getAllAppenixesOfTypeAsList(Appendix_B2.APPENDIX_B2_ID);
		assertThat(b2List)
				.isNotNull()
				.hasSize(2);
		final Appendix_B2 appendix_B2 = b2List.get(0);
		assertThat(appendix_B2.getAString())
				.isEqualTo(anotherTestString);
	}

	private MethodImplementation createImplementation() throws NoSuchMethodException {
		final MethodImplementation implementation = MethodImplementation.builder()
				.methodIdentifier(this.emulatedMethodIdentifier)
				.returnValueType(String.class)
				.beanOperation(BeanOperation.builder()
						.implementation(new TestBean_1())
						.methodIdentifier("doSomething")
						.argument(new FunctionSignatureArgument(String.class, 0))
						.argument(new FunctionSignatureArgument(int.class, 1))
						.argument(new FunctionSignatureArgument(Integer.class, 2))
						.build())
				.beanOperation(BeanOperation.builder()
						.implementation(new TestBean_2())
						.methodIdentifier("anotherAction")
						.argument(new FunctionSignatureArgument(String.class, 3))
						.argument(new ApplicationProtocolArgument())
						.build())
				.build();
		implementation
				.method(EmulatedInterface.class.getDeclaredMethod(this.emulatedMethodIdentifier, String.class,
						int.class,
						Integer.class, String.class));
		return implementation;
	}

	// ------------------------------------------------------------------------//

	public interface EmulatedInterface {

		ApplicationProtocol<String> operation(String aString, int anInt, Integer anInteger, String anotherString);
	}

	private final static Owner_1 owner1 = new Owner_1();

	private static class Owner_1 extends AppendixOwner {

		public Owner_1() {
			super(UUID.randomUUID());
		}

		public Appendix_A1 createAppendix_A1(final int anInt, final Integer anInteger) {
			return new Appendix_A1(getOwnUuid(), anInt, anInteger);
		}
	}

	@Getter
	private static class Appendix_A1 extends ApplicationAppendix {

		private final int anInt;
		private final Integer anInteger;
		private final static UUID APPENDIX_A1_ID = UUID.randomUUID();

		public Appendix_A1(final UUID owner, final int anInt, final Integer anInteger) {
			super(APPENDIX_A1_ID, owner);
			this.anInt = anInt;
			this.anInteger = anInteger;
		}
	}

	private static class TestBean_1 implements ApplicationFrameworkInvokable {

		@SuppressWarnings("unused")
		public ApplicationProtocol<String> doSomething(final String aString, final int anInt, final Integer anInteger) {
			final ApplicationProtocol<String> state = new ApplicationProtocol<>();
			state.setResult(aString);
			state.addAppendix(MethodImplementationTest.owner1.createAppendix_A1(anInt, anInteger));

			return state;
		}
	}

	private final static Owner_2 owner2 = new Owner_2();

	private static class Owner_2 extends AppendixOwner {

		public Owner_2() {
			super(UUID.randomUUID());
		}

		public Appendix_A2 createAppendix_A2(final String aString) {
			return new Appendix_A2(getOwnUuid(), aString);
		}

		public Appendix_B2 createAppendix_B2(final String aString) {
			return new Appendix_B2(getOwnUuid(), aString);
		}
	}

	@Getter
	private static class Appendix_A2 extends ApplicationAppendix {

		private final String aString;
		private final static UUID APPENDIX_A2_ID = UUID.randomUUID();

		public Appendix_A2(final UUID owner, final String aString) {
			super(APPENDIX_A2_ID, owner);
			this.aString = aString;
		}
	}

	@Getter
	private static class Appendix_B2 extends ApplicationAppendix {

		private final String aString;
		private final static UUID APPENDIX_B2_ID = UUID.randomUUID();

		public Appendix_B2(final UUID owner, final String aString) {
			super(APPENDIX_B2_ID, owner);
			this.aString = aString;
		}
	}

	private class TestBean_2 implements ApplicationFrameworkInvokable {

		@SuppressWarnings("unused")
		public ApplicationProtocol<String> anotherAction(final String anotherString,
				final ApplicationProtocol<String> state) {
			state.setResult(state.getResult() + anotherString);
			state.addAppendix(MethodImplementationTest.owner2.createAppendix_A2(anotherString));
			state.addAppendix(MethodImplementationTest.owner2.createAppendix_B2(anotherString));
			state.addAppendix(MethodImplementationTest.owner2.createAppendix_B2(anotherString));

			return state;
		}
	}
}
