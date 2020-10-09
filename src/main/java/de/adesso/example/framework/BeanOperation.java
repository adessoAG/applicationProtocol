package de.adesso.example.framework;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

import com.sun.istack.NotNull;

import lombok.Builder;
import lombok.Getter;
import lombok.Singular;
import lombok.extern.log4j.Log4j2;

/**
 * This class describes how a method of a class should be called within the
 * framework. The requirements of the framework are that the result type is
 * {@link ApplicationProtocol}. There may be some parameters which can be given
 * from the call by {@link FunctionSignatureArgument} or from the list of
 * appendixes by the class {@link ArgumentFromAppendix}.
 * 
 * @author Matthias
 *
 */
@Builder
@Getter
@Log4j2
public class BeanOperation {

	// identifier of the operation
	private String methodIdentifier;
	// object which provides the requested method
	private ApplicationFrameworkInvokable implementation;
	// Operation list to be executed
	@NotNull
	private Method method;
	// parameters
	@Singular
	private List<Argument> arguments;

	/**
	 * @param proxy
	 * @param state
	 * @param args
	 * @return
	 */
	public ApplicationProtocol<?> execute(ApplicationProtocol<?> state, Object[] args) {

		Object[] methodArguments = prepareArguments(state, args);
		ApplicationProtocol<?> result = null;
		try {
			result = (ApplicationProtocol<?>) this.method.invoke(implementation, methodArguments);
			
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			final String message = String.format("could not invoke configured target bean (%s::%s)",
					implementation.getClass().getName(), method.getName());
			log.atError().log(message);
			throw new ClassCastException(message);
		}
		
		return result;
	}

	private Object[] prepareArguments(ApplicationProtocol<?> state, Object[] args) {
		this.arguments.stream()
				.map(a -> a.prepareArgument(state, args));

		return null;
	}
}
