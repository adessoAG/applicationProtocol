package de.adesso.example.framework.core;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.util.Assert;

import de.adesso.example.framework.ApplicationProtocol;
import de.adesso.example.framework.annotation.CallStrategy;
import de.adesso.example.framework.exception.BuilderException;
import de.adesso.example.framework.exception.CalculationNotApplicable;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.Singular;
import lombok.ToString;
import lombok.extern.log4j.Log4j2;

/**
 * This class describes how a method of a class should be called within the
 * framework. The requirements of the framework are that the result type is
 * {@link ApplicationProtocol}. There may be some parameters which can be given
 * from the call by {@link ArgumentFromMethod} or from the list of appendixes by
 * the class {@link ArgumentFromAppendix}.
 * <p>
 * Since the class variable needs to be evaluated, it is not possible to work
 * just with the bean name. The operation is established in a very early phase
 * of the system initialization. During this time it is not possible to load
 * beans from the application context.
 *
 * @author Matthias
 *
 */
@Getter(value = AccessLevel.PACKAGE)
@Log4j2
@ToString
public class BeanOperation {

	/** identifier of the operation */
	private final String methodIdentifier;

	/**
	 * Interface of the bean to be used during execution. The class will look up the
	 * bean from the application context. Since emulated interfaces are populated to
	 * the context they can also used this way.
	 */
	private final Class<?> anInterface;

	/**
	 * Class variable of the bean. This can be used, if a Spring bean does not
	 * implement an appropriate interface. Implementing an interface is the better
	 * solution. The bean will be accessed via the application context. This
	 * procedure enables Spring to instrument the bean which would not be possible
	 * if the corresponding object would be used.
	 */
	private Class<Object> beanType;

	/**
	 * The implementation is an object which will be used during execution of the
	 * described operation.
	 */
	private Object implementation;

	/** The given method is the operation which will be used during execution. */
	private Method method;

	/**
	 * List of the arguments of the operation.
	 */
	private final List<Argument> arguments;

	private transient CallStrategy callStrategy;
	private transient MethodImplementation methodImplementation;

	@Builder
	private BeanOperation(final String methodIdentifier, final Class<?> anInterface, final Class<Object> beanType,
			final Object implementation, @Singular final List<Argument> arguments, final Method method) {
		Assert.notNull(arguments, "arguments are required to build the configuration");
		Assert.notNull(methodIdentifier, "method identifier is required to instantiate");

		this.methodIdentifier = methodIdentifier;
		this.anInterface = anInterface;
		this.beanType = beanType;
		this.implementation = implementation;
		this.method = null;
		this.arguments = arguments;
		this.method = method;

		if (method == null) {
			this.setMethod(this.getDescribingClass());
		}
	}

	/**
	 * Since it is allowed to use an interface during the instantiation, it is
	 * necessary to provide the object to work on.
	 *
	 * @param methodImplementation
	 *
	 * @param context              application context to load beans
	 */
	@SuppressWarnings("unchecked")
	public void init(final MethodImplementation methodImplementation, final ApplicationContext context) {
		this.methodImplementation = methodImplementation; // parent

		// if the implementation is provided, nothing to do.
		if (this.implementation == null) {
			// validate information is available
			if (this.beanType == null && this.anInterface == null) {
				final String message = String.format("no type given for implementation bean %s", this.methodIdentifier);
				log.atError().log(message);
				throw new NullPointerException(message);
			}

			// determine the type
			try {
				if (this.beanType != null) {
					this.implementation = context.getBean(this.beanType);
				} else if (this.anInterface != null) {
					this.implementation = context.getBean(this.anInterface);
				}
			} catch (final BeansException e) {
				// ignore that exception. It might be a POJO.
				log.atDebug().log("given type is not a valid bean: {}, try classloader",
						this.beanType != null ? this.beanType.getName() : this.anInterface.getName());
				try {
					this.implementation = this.beanType.getDeclaredConstructor().newInstance();
				} catch (InstantiationException | IllegalAccessException | IllegalArgumentException
						| InvocationTargetException | NoSuchMethodException | SecurityException e1) {
					final String message = String.format(
							"finally could not provide class, class loader failed to load %s", this.methodIdentifier);
					log.atError().log(message, e1);
					throw new BuilderException(message, e1);
				}
			}
		}
		Assert.notNull(this.implementation, "one of implementation, beanType, anInterface is required");
		if (this.beanType == null) {
			this.beanType = (Class<Object>) this.implementation.getClass();
		}

		// provide the target position to the arguments
		IntStream.range(0, this.arguments.size())
				.forEach(i -> this.arguments.get(i).init(this, this.method.getParameters()[i], i));
	}

	/**
	 * execute the described method.
	 *
	 * @param state application protocol instance
	 * @param args  arguments
	 * @return the updated application protocol instance
	 */
	public ApplicationProtocol<?> execute(final ApplicationProtocol<?> state, final Object[] args) {

		final Object[] methodArguments;
		try {
			methodArguments = this.prepareArguments(state, args);
		} catch (final CalculationNotApplicable e) {
			return state; // bean has to be called, if required parameters are present
		}

		ApplicationProtocol<?> result = null;
		try {
			result = (ApplicationProtocol<?>) this.method.invoke(this.implementation, methodArguments);

		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			final String message = String.format("could not invoke configured target bean (%s::%s)",
					this.implementation.getClass().getName(), this.method.getName());
			log.atError().log(message);
			throw new ClassCastException(message);
		}

		return result;
	}

	private Object[] prepareArguments(final ApplicationProtocol<?> state, final Object[] args) {
		final Object[] result = this.arguments.stream()
				.map(a -> a.prepareArgument(state, args))
				.collect(Collectors.toList()).toArray();

		return result;
	}

	private Class<?> getDescribingClass() {
		Class<?> describingClass = null;
		if (this.implementation != null) {
			describingClass = this.implementation.getClass();
		}

		// if the implementation is provided, nothing to do.
		if (describingClass == null) {
			if (this.beanType != null) {
				describingClass = this.beanType;
			} else if (this.anInterface != null) {
				describingClass = this.anInterface;
			}
		}
		Assert.notNull(describingClass, "one of implementation, beanType, anInterface is required");

		return describingClass;
	}

	private void setMethod(final Class<?> describingClass) {
		try {
			log.atDebug().log("going to extract method {}::{}", describingClass.getName(),
					this.methodIdentifier);
			this.method = describingClass.getDeclaredMethod(this.methodIdentifier,
					this.argumentTypes(this.arguments));
		} catch (NoSuchMethodException | SecurityException e) {
			final String message = "could not build bean operation";
			log.atError().log(message, e);
			throw new BuilderException(message, e);
		}

		// get the annotations from the method
		this.callStrategy = this.method.getDeclaredAnnotation(CallStrategy.class);
	}

	private Class<?>[] argumentTypes(final List<Argument> arguments) {
		final List<Class<?>> resultTypes = arguments.stream()
				.map(Argument::getType)
				.collect(Collectors.toList());
		return resultTypes.toArray(new Class<?>[resultTypes.size()]);
	}
}
