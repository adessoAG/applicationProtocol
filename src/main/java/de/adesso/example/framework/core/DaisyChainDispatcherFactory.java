/**
 * The MIT License (MIT)
 *
 * Copyright © 2020 Matthias Brenner and Adesso SE
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the “Software”), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED “AS IS”, WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *
 */
package de.adesso.example.framework.core;

import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import de.adesso.example.framework.exception.UnknownMethodException;
import lombok.extern.log4j.Log4j2;

/**
 * The class implements an infrastructure which is capable to call during a
 * method call on the emulated interface several methods of implementing beans.
 * This supports separation of concerns. The participating beans belong to
 * probably different business domains. They are invoked if the required
 * parameters are available.
 * <p>
 * The emulation is achieved via an invocation handler and a proxy. The calls to
 * the provided implementations are described by {@link MethodImplementation},
 * {@link BeanOperation} and {@link Argument}.
 * <p>
 * This class is the factory which establishes the functionality.
 *
 * @author Matthias
 *
 */
@Log4j2
public class DaisyChainDispatcherFactory {

	private final Map<String, MethodImplementation> emulateMethods = new HashMap<>();
	private Class<?> implementationInterface;
	private Map<String, Method> interfaceMethods = new HashMap<>();
	private final ClassLoader classLoader;

	public DaisyChainDispatcherFactory(final ApplicationContext applicationContext) {
		this.classLoader = applicationContext.getClassLoader();
	}

	//
	// builder property methods
	//

	/**
	 * The DispatcherBean will emulate some kind of interface. This is the call to
	 * provide the type of interface to emulate.
	 *
	 * @param anInterface the interface which will be emulated.
	 * @throws ClassCastException if the given type is not an interface
	 * @return the factory for chained construction
	 */
	public DaisyChainDispatcherFactory emulationInterface(final Class<?> anInterface) {
		// validate the parameters
		if (!anInterface.isInterface()) {
			throw new ClassCastException(
					"Implementation is required to be an interface: " + anInterface.getClass().getName());
		}
		// validate the state
		if (this.emulateMethods.size() != 0) {
			final String message = "wrong ordering of calls, implementation is not empty";
			log.atError().log(message);
			throw new IllegalStateException(message);
		}

		// set the interface
		this.implementationInterface = anInterface;
		// save the contained methods of the interface
		this.interfaceMethods = Arrays.asList(anInterface.getDeclaredMethods())
				.stream()
				.collect(Collectors.toMap(Method::getName, m -> m));
		return this;
	}

	/**
	 * With help of this method, the emulation of the interface method is generated.
	 * The information will be used by the dispatcher to emulate the functionality
	 *
	 * @param implementation implementation description of a method
	 * @return the factory itself for chained construction
	 * @throws UnknownMethodException if the implementation describes a method which
	 *                                is not part of the implementation interface
	 */
	public DaisyChainDispatcherFactory implementation(final MethodImplementation implementation) {
		// validate the operation
		if (!this.interfaceMethods.containsKey(implementation.getMethodIdentifier())) {
			final String message = "cannot emulate methods not provided by the interface to be implemented";
			log.atError().log(message);
			throw new UnknownMethodException(message);
		}
		// provide the reflective method to the implementation
		implementation.method(this.interfaceMethods.get(implementation.getMethodIdentifier()));

		// keep the implementation
		this.emulateMethods.put(implementation.getMethodIdentifier(), implementation);
		return this;
	}

	@SuppressWarnings("unchecked")
	public <T> T build() {
		// create the dispatcher and feed collected information
		final DaisyChainDispatcher dispatcher = new DaisyChainDispatcher(
				this.implementationInterface, this.emulateMethods);

		// create the proxy, ApplicationContextAware is required, because beans might
		// have to be loaded, InitializingBean because the initialization needs to be
		// finished.
		final T proxy = (T) Proxy.newProxyInstance(this.classLoader,
				new Class[] { this.implementationInterface, ApplicationContextAware.class, InitializingBean.class },
				dispatcher);

		return proxy;
	}
}
