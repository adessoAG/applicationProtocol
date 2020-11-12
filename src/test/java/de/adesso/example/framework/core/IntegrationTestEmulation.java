package de.adesso.example.framework.core;

import static org.assertj.core.api.Assertions.assertThat;

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
import de.adesso.example.framework.TestOtherSpringBeanImpl;
import de.adesso.example.framework.TestPojoBean;

@RunWith(SpringRunner.class)
@SpringBootTest
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

		assertThat(state.getResult()).isEqualTo(TestPojoBean.testString);
	}

}
