package de.adesso.example.framework;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.fail;

import java.lang.reflect.Method;

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

	@Test
	public void testCreateArgumentByType() {
		fail("Not yet implemented");
	}

	private interface ToBeEmulated {

		ApplicationProtocol<String> operation(String aString, int aInt, ApplicationProtocol<String> state);
	}

	private static class Bean {

		public ApplicationProtocol<String> doSomething(final String aString) {
			final ApplicationProtocol<String> result = new ApplicationProtocol<>();
			result.setResult(aString);

			return result;
		}
	}
}
