package de.adesso.example.framework;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;

/**
 * The class can take a configuration during its initialization (builder
 * pattern). At execution time the configuration is executed to achieve the
 * required result. Therefore the execution depends on the configuration
 * provided.
 * <p>
 * The class is a Spring bean. It is allocated as prototype, because every usage
 * requires its own configuration.
 * <p>
 * The class uses the builder pattern provided by Lombok. This ensures, that the
 * class configuration is established during instantiation.
 * <p>
 * The class implements the InvocationHandler (from reflection) to be able to
 * implement an interface provided during initialization.
 *
 * @author Matthias
 *
 * @param <INTERFACE> represents the functional interface which should return
 *                    the final result.
 * @param RETURN_TYPE type of the return value
 */
@Getter(value = AccessLevel.PACKAGE)
@Setter(value = AccessLevel.PACKAGE)
@Log4j2
public class DaisyChainDispatcher implements InvocationHandler {

	private Class<?> implementationInterface;
	private Map<String, MethodImplementation> emulateMethods = new HashMap<>();

	/**
	 * Implement the requirements of interface {@link InvocationHandler}.
	 * <p>
	 * Provides the specific semantic of {@link ApplicationProtocol}. If the last
	 * object within the list args is of type {@link ApplicationProtocol} the
	 * instance is used. Otherwise a new object is created. The
	 * {@link ApplicationProtocol} is provided to the implementation beans as last
	 * parameter. The bean provides the updated protocol as return value. The
	 * protocol structure also contains the calculation result.
	 */
	@Override
	public Object invoke(final Object proxy, final Method method, final Object[] args) throws Throwable {

		// get the implementation
		final MethodImplementation implementation = this.emulateMethods.get(method.getName());
		if (implementation == null) {
			// there is no implementation, therefore this method is not provided.
			log.atWarn().log("no implementation for interface %s::method %s",
					this.implementationInterface.getName(),
					method.getName());
			return null;
		}

		// get the protocol
		ApplicationProtocol<?> state = createOrExtractProtocolFrom(args);

		// execute the emulation
		state = implementation.execute(state, args);

		return state;
	}

	private ApplicationProtocol<?> createOrExtractProtocolFrom(final Object[] args) {
		final Object lastArgument = lastArgument(args);
		@SuppressWarnings("rawtypes")
		final ApplicationProtocol<?> state = lastArgument != null && lastArgument instanceof ApplicationProtocol
				? (ApplicationProtocol) lastArgument
				: new ApplicationProtocol();
		return state;
	}

	private Object lastArgument(final Object[] args) {
		return args.length > 0 ? args[args.length - 1] : null;
	}
}
