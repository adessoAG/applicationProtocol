package de.adesso.example.framework;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import de.adesso.example.framework.exception.UnknownMethodException;
import lombok.Getter;

@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = { TestConfig.class })
public class DaisyChainDispatcherFactoryTest {

	@Autowired
	private DaisyChainDispatcherFactory factory;

	@Test
	public void testBuild() {
		final EmulatedInterface emulated = createProxy();

		assertThat(emulated)
				.isNotNull();
	}

	@Test
	public void testOperationOnEmulatedInterface() {
		final EmulatedInterface emulated = createProxy();

		final String testString = "So sieht der String aus. ";
		final Integer testInteger = 13;
		final int testInt = 5;
		final String anotherTestString = "Das ist ein Teststring";
		final ApplicationProtocol<String> resultState = emulated.operation(testString, testInt, testInteger,
				anotherTestString);

		assertThat(resultState.getResult())
				.isEqualTo(testString + anotherTestString);

		final Optional<ApplicationAppendix<?>> optionalA1 = resultState.getAppendixOfClass(Appendix_A1.class);
		assertThat(optionalA1)
				.isNotNull()
				.isNotEmpty();
		final ApplicationAppendix<?> a1 = optionalA1.get();
		assertThat(a1)
				.isInstanceOf(Appendix_A1.class);
		final Appendix_A1 appendix_A1 = (Appendix_A1) a1;
		assertThat(appendix_A1.getContent())
				.isEqualTo(5);

		final Optional<ApplicationAppendix<?>> optionalA2 = resultState.getAppendixOfClass(Appendix_A2.class);
		assertThat(optionalA2)
				.isNotNull()
				.isNotEmpty();
		final ApplicationAppendix<?> a2 = optionalA2.get();
		assertThat(a2)
				.isInstanceOf(Appendix_A2.class);
		final Appendix_A2 appendix_A2 = (Appendix_A2) a2;
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

	@Test(expected = ClassCastException.class)
	public void testNotInterface() {
		this.factory.implementationInterface(Wrong.class);
	}

	@Test(expected = UnknownMethodException.class)
	public void testWrongOrdering() {
		this.factory.operation(MethodImplementation.builder()
				.methodIdentifier(EmulatedInterface.method_1)
				.returnValueType(String.class)
				.beanOperation(BeanOperation.builder()
						.implementation(new TestBean_1())
						.methodIdentifier("doSomething")
						.argument(new MethodArgument(String.class, 0))
						.argument(new MethodArgument(int.class, 1))
						.argument(new MethodArgument(Integer.class, 2))
						.build())
				.build());
	}

	// ------------------------------------------------------------------------//

	private EmulatedInterface createProxy() {
		final EmulatedInterface emulated = this.factory
				.implementationInterface(EmulatedInterface.class)
				.operation(MethodImplementation.builder()
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
						.build())
				.operation(MethodImplementation.builder()
						.methodIdentifier(EmulatedInterface.method_2)
						.returnValueType(String.class)
						.beanOperation(BeanOperation.builder()
								.implementation(new TestBean_2())
								.methodIdentifier("anotherAction")
								.argument(new MethodArgument(String.class, 3))
								.argument(new ApplicationProtocolAsArgument())
								.build())
						.build())
				.build();
		return emulated;
	}

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

		public Appendix_A1(final Integer content) {
			super(content);
		}

		@Override
		public UUID getOwner() {
			return owner1.getOwner();
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

		public Appendix_A2(final String content) {
			super(content);
		}

		@Override
		public UUID getOwner() {
			return owner2.getOwner();
		}
	}

	@Getter
	public static class Appendix_B2 extends ApplicationAppendix<String> {

		public Appendix_B2(final String content) {
			super(content);
		}

		@Override
		public UUID getOwner() {
			return owner2.getOwner();
		}
	}

	public class TestBean_2 implements ApplicationFrameworkInvokable {

		public ApplicationProtocol<String> anotherAction(final String anotherString,
				final ApplicationProtocol<String> state) {
			state.setResult(state.getResult() + anotherString);
			state.addAppendix(new Appendix_A2(anotherString));
			state.addAppendix(new Appendix_B2(anotherString));
			state.addAppendix(new Appendix_B2(anotherString));

			return state;
		}
	}

	@SuppressWarnings("unused")
	private static class Wrong {

		ApplicationProtocol<String> operation(final String aString) {
			return null;
		}
	}
}
