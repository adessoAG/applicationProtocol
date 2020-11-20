package de.adesso.example.framework.core;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import de.adesso.example.framework.AnnotatedEmulatedInterfaceOtherEmulatedInterface;
import de.adesso.example.framework.AnnotatedEmulatedInterfacePojo;
import de.adesso.example.framework.AnnotatedEmulatedInterfaceSpringBean;
import de.adesso.example.framework.AnnotatedEmulatedInterfaceSpringBeanInterface;
import de.adesso.example.framework.ApplicationProtocol;
import de.adesso.example.framework.TestConfig;
import de.adesso.example.framework.TestOtherSpringBeanImpl;
import de.adesso.example.framework.exception.BeanCallException;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = { TestConfig.class })
public class IntegrationTestEmulation {

	@Autowired
	AnnotatedEmulatedInterfaceSpringBean springBeanBased;

	@Autowired
	AnnotatedEmulatedInterfaceSpringBeanInterface springBeanBasedInterface;

	@Autowired
	AnnotatedEmulatedInterfaceOtherEmulatedInterface otherEmulatedInterface;

	@Autowired
	AnnotatedEmulatedInterfacePojo pojoBased;

	@Test
	public void testImplementationIsSpringBean() {
		ApplicationProtocol<String> state = new ApplicationProtocol<>();
		final String testString = "test string";

		// perform the test
		state = this.springBeanBased.operation(testString, state);

		assertThat(state.getResult()).isEqualTo(testString);
	}

	@Test
	public void testImplementationIsInterfaceOfSpringBean() {
		ApplicationProtocol<String> state = new ApplicationProtocol<>();
		final String testString = "test string";

		// perform the test
		state = this.springBeanBasedInterface.operation(testString, state);

		assertThat(state.getResult()).isEqualTo(testString);
	}

	@Test
	public void testImplementationIsInterfaceOfEmulatedInterface() {
		ApplicationProtocol<String> state = new ApplicationProtocol<>();
		final String testString = "test string";

		// perform the test
		state = this.otherEmulatedInterface.operation(testString, state);

		assertThat(state.getResult()).isEqualTo(TestOtherSpringBeanImpl.testString);
	}

	@Test
	public void testImplementationIsPojo() {
		ApplicationProtocol<String> state = new ApplicationProtocol<>();
		final String testString = "test string";

		// perform the test
		state = this.pojoBased.operation(testString, state);

		assertThat(state.getResult()).isEqualTo(testString);
	}

	@Test
	public void testCallAllowsNull() {
		ApplicationProtocol<String> state = new ApplicationProtocol<>();

		// perform the test
		state = this.pojoBased.operation(null, state);

		assertThat(state.getResult()).isNull();
	}

	@Test(expected = NullPointerException.class)
	public void testParameterRequiredNotNull() {
		final ApplicationProtocol<String> state = new ApplicationProtocol<>();

		// perform the test
		this.pojoBased.requiredParameterOperation(null, state);
	}

	@Test(expected = NullPointerException.class)
	public void testStateRequiredNotNull() {
		final String testString = "test string";

		// perform the test
		this.pojoBased.requiredParameterOperation(testString, null);
	}

	@Test(expected = BeanCallException.class)
	public void testParameterListRequiredNotEmpty() {
		final ApplicationProtocol<String> state = new ApplicationProtocol<>();
		final ArrayList<String> aList = new ArrayList<>();

		// perform the test
		this.pojoBased.listOperation(aList, state);
	}

	@Test(expected = BeanCallException.class)
	public void testBeanException() {
		ApplicationProtocol<String> state = new ApplicationProtocol<>();
		final String testString = "test string";

		// perform the test
		state = this.pojoBased.throwingOperation(testString, state);
	}

	@Test(expected = BeanCallException.class)
	public void testDetectionOfNullExtraction() {
		ApplicationProtocol<String> state = new ApplicationProtocol<>();

		// perform the test
		state = this.pojoBased.operationOtherType(null, state);
	}

	@Test(expected = BeanCallException.class)
	public void testDetectionOfNullExtraction_2() {
		ApplicationProtocol<String> state = new ApplicationProtocol<>();

		// perform the test
		state = this.pojoBased.operationOtherType_2(null, state);
	}
}
