package de.adesso.example.framework;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.sun.istack.NotNull;

import lombok.Builder;
import lombok.Getter;
import lombok.Singular;

@Getter
public class MethodImplementation {

	/**
	 * Identifier of the method to be implemented
	 */
	@NotNull
	private final String methodIdentifier;
	/**
	 * Template type of the {@link ApplicationProtocol}
	 */
	@NotNull
	private final Class<?> returnValueType;
	/**
	 * Operations to be performed to implemented that method
	 */
	@NotNull
	private final List<BeanOperation> beanOperations;
	/**
	 * Method of the interface which is implemented by this description
	 */
	@NotNull
	private Method method;
	/**
	 * The following information is derived from the method. Collection of the
	 * formal parameters to be able to process the parameters during a call.
	 */
	private List<Parameter> parameterTypes = new ArrayList<>();

	
	@Builder
	public MethodImplementation(
			String methodIdentifier, 
			Class<?> returnValueType, 
			@Singular List<BeanOperation> beanOperations) {
		this.methodIdentifier = methodIdentifier;
		this.returnValueType = returnValueType;
		this.beanOperations = beanOperations;
	}
	
	public ApplicationProtocol<?> execute(Object proxy, ApplicationProtocol<?> state, Object[] args) {

		validateParameters(args);
		ApplicationProtocol<?> intermediateState = state;

		// call all bean methods defined
		for (BeanOperation o : this.beanOperations) {
			intermediateState = o.execute(state, args);
		}

		return intermediateState;
	}

	private void validateParameters(Object[] args) throws ClassCastException {

		for (int i = 0; i < args.length; i++) {
			if (!this.parameterTypes.get(i).getClass().isAssignableFrom(args[i].getClass())) {
				throw new ClassCastException();
			}
		}
	}

	/**
	 * The method is derived from the interface. Within the interface the method is
	 * selected by the configured method identifier. Therefore this information is
	 * available later during construction of the calling syntax.
	 * 
	 * @param method the implemented method
	 * 
	 * @return itself for chained execution
	 */
	MethodImplementation setMethod(final Method method) {
		this.method = method;

		// extract a description of all arguments of the method
		this.parameterTypes = Arrays.asList(method.getParameters());

		return this;
	}
}
