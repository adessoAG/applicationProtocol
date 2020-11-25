package de.adesso.example.framework.core;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import de.adesso.example.framework.ApplicationOwner;
import de.adesso.example.framework.ApplicationProtocol;
import de.adesso.example.framework.EmulatedInterface;
import de.adesso.example.framework.TestConfig;
import de.adesso.example.framework.exception.UnknownMethodException;
import lombok.AllArgsConstructor;

@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = { TestConfig.class })
public class DaisyChainDispatcherFactoryTest {

	@Autowired
	private ApplicationContext context;

	@Test
	public void testBuild() throws Exception {
		final EmulatedInterface emulated = this.createProxy();

		assertThat(emulated)
				.isNotNull();
	}

	@Test
	public void testOperationOnEmulatedInterface() throws Exception {
		final EmulatedInterface emulated = this.createProxy();

		final String testString = "So sieht der String aus. ";
		final Integer testInteger = 13;
		final int testInt = 5;
		final String anotherTestString = "Das ist ein Teststring";
		final ApplicationProtocol<String> resultState = emulated.operation(testString, testInt, testInteger,
				anotherTestString);

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

	@Test(expected = ClassCastException.class)
	public void testNotInterface() {
		new DaisyChainDispatcherFactory(this.context)
				.emulationInterface(Wrong.class);
	}

	@Test(expected = UnknownMethodException.class)
	public void testWrongOrdering() {
		new DaisyChainDispatcherFactory(this.context)
				.implementation(MethodImplementation.builder()
						.methodIdentifier(EmulatedInterface.method_1)
						.beanOperation(BeanOperation.builder()
								.implementation(new TestBean_1())
								.methodIdentifier("doSomething")
								.argument(new ArgumentFromMethod(String.class, 0))
								.argument(new ArgumentFromMethod(int.class, 1))
								.argument(new ArgumentFromMethod(Integer.class, 2))
								.build())
						.build());
	}

	// ------------------------------------------------------------------------//

	private EmulatedInterface createProxy() throws Exception {
		final EmulatedInterface emulated = new DaisyChainDispatcherFactory(this.context)
				.emulationInterface(EmulatedInterface.class)
				.implementation(MethodImplementation.builder()
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
						.build())
				.implementation(MethodImplementation.builder()
						.methodIdentifier(EmulatedInterface.method_2)
						.beanOperation(BeanOperation.builder()
								.implementation(new TestBean_2())
								.methodIdentifier("anotherAction")
								.argument(new ArgumentFromMethod(String.class, 0))
								.argument(new ArgumentApplicationProtocol())
								.build())
						.beanOperation(BeanOperation.builder()
								.implementation(new Wrong())
								.methodIdentifier("operation")
								.argument(new ArgumentFromMethod(String.class, 0))
								.build())
						.build())
				.build();
		if (emulated instanceof InitializingBean) {
			final InitializingBean bean = (InitializingBean) emulated;
			bean.afterPropertiesSet();
		}
		return emulated;
	}

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

	@SuppressWarnings("unused")
	private static class Wrong {

		ApplicationProtocol<String> operation(final String aString) {
			return null;
		}
	}
}
