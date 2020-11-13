package de.adesso.example.framework.core;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.TypeVariable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.util.Assert;

import de.adesso.example.framework.ApplicationProtocol;
import de.adesso.example.framework.annotation.Emulated;
import de.adesso.example.framework.annotation.Implementation;
import de.adesso.example.framework.annotation.MatchingStrategy;
import de.adesso.example.framework.core.BeanOperation.BeanOperationBuilder;
import de.adesso.example.framework.core.MethodImplementation.MethodImplementationBuilder;
import de.adesso.example.framework.exception.BuilderException;
import de.adesso.example.framework.exception.GenericDeclarationException;
import de.adesso.example.framework.exception.MissingAnnotationException;
import de.adesso.example.framework.exception.UndefinedParameterException;
import lombok.NonNull;
import lombok.extern.log4j.Log4j2;

/**
 * This class creates the factory for a proxy implementing an application
 * framework supported interface. It is not possible to provide this class as
 * Spring service, because it has to be instantiated early during the
 * initialization process which makes things a little more complicated. The
 * class cannot trust on injection of dependencies. All has to be provided
 * during instantiation.
 *
 * @author Matthias
 *
 */
@Log4j2
public class ApplicationProxyFactory implements FactoryBean<Object>, ApplicationContextAware {

	private final Class<Object> emulatedInterface;
	private Object generatedEmulation;
	private final ArgumentFactory argumentFactory;
	private ApplicationContext applicationContext;

	public ApplicationProxyFactory(final ArgumentFactory argumentFactory,
			final Class<Object> emulatedInterface) {
		Assert.notNull(emulatedInterface, "the emulated interface may not be null");
		this.argumentFactory = argumentFactory;
		this.emulatedInterface = emulatedInterface;
	}

	@Override
	public void setApplicationContext(final ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
		applicationContext.getClassLoader();
	}

	public Object getObject() throws Exception {
		if (this.generatedEmulation == null) {
			this.generatedEmulation = this.emulateInterface(this.emulatedInterface);
			if (this.generatedEmulation instanceof ApplicationContextAware) {
				final ApplicationContextAware aca = (ApplicationContextAware) this.generatedEmulation;
				aca.setApplicationContext(this.applicationContext);
			}
			if (this.generatedEmulation instanceof InitializingBean) {
				final InitializingBean ib = (InitializingBean) this.generatedEmulation;
				ib.afterPropertiesSet();
			}
		}
		return this.generatedEmulation;
	}

	public Class<Object> getObjectType() {
		return this.emulatedInterface;
	}

	private Object emulateInterface(@NonNull final Class<Object> interfaceType) {
		this.validateClassAnnotations(interfaceType);

		// initialize the factory to build the proxy
		final DaisyChainDispatcherFactory factory = new DaisyChainDispatcherFactory(this.applicationContext)
				.emulationInterface(interfaceType);

		// add the methods of the interface to be emulated
		this.processAllMethods(interfaceType).stream()
				.forEach(m -> factory.implementation(m));

		return factory.build();
	}

	private <T> Collection<MethodImplementation> processAllMethods(final Class<T> interfaceType) {
		final List<MethodImplementation> implementations = new ArrayList<>();
		for (final Method m : interfaceType.getMethods()) {
			final MethodImplementation methodEmulation = this.buildMethodEmulation(m);
			methodEmulation.method(m);
			implementations.add(methodEmulation);
		}
		return implementations;
	}

	@SuppressWarnings("unchecked")
	private MethodImplementation buildMethodEmulation(final Method interfaceMethod) {
		Assert.notNull(interfaceMethod, "method is mandatory argument");

		final String methodName = interfaceMethod.getName();
		final MethodImplementationBuilder miBuilder = MethodImplementation.builder()
				.methodIdentifier(methodName)
				.returnValueType(this.extractReturnValueType(interfaceMethod));

		// the interface may annotate several beans to provide implementations which
		// will be called consecutively
		final Implementation implAnnotation = interfaceMethod.getAnnotation(Implementation.class);
		final MatchingStrategy[] strategies = this.buildStrategy(implAnnotation.strategy());
		for (final Class<?> implClass : implAnnotation.implementations()) {
			final BeanOperationBuilder operationBuilder = BeanOperation.builder();
			if (implClass.isInterface()) {
				operationBuilder.anInterface(implClass);
			} else {
				operationBuilder.beanType((Class<Object>) implClass);
			}
			operationBuilder.methodIdentifier(methodName);
			final Method beanMethod = this.extractCorrespondingBeanMethod(methodName, implClass);
			if (beanMethod == null) {
				// basic requirement: the implementation bean provides at least one method with
				// the same identifier
				final String message = String.format("could not locate the requested method %s::%s",
						implClass.getName(), methodName);
				log.atError().log(message);
				throw new BuilderException(message);
			}
			operationBuilder.method(beanMethod);

			// found appropriate method, now instrument it
			final List<Argument> argumentList = new ArrayList<>();
			for (final Parameter p : beanMethod.getParameters()) {
				// find a provider for each parameter
				Argument argument = null;
				for (final MatchingStrategy strategy : strategies) {
					try {
						switch (strategy) {
						case ByType:
							argument = this.argumentFactory.createArgumentByType(interfaceMethod, beanMethod,
									p.getType());
							break;
						case ByName:
							argument = this.argumentFactory.createArgumentByName(interfaceMethod, beanMethod,
									p.getName());
							break;
						case FromAppendix:
							argument = this.argumentFactory.createArgumentFromAppendix(beanMethod, p.getType());
							break;
						default:
							// should not be reached
							final String message = String.format("missing implementation for strategy %s", strategy);
							log.atFatal().log(message);
							throw new RuntimeException(message);
						}
					} catch (final Throwable e) {
						// strategy could not generate the parameter
						continue;
					}
					if (argument != null) {
						argumentList.add(argument);
						break;
					}
				}
				if (argument == null) {
					// could not find any content for the argument
					final String message = String.format("could not provide bean %s with argument %s of type %s",
							implClass.getName(), p.getName(), p.getType().getName());
					log.atFatal().log(message);
					throw new UndefinedParameterException(message);
				}
			}
			operationBuilder.arguments(argumentList);
			miBuilder.beanOperation(operationBuilder.build());
		}
		return miBuilder.build();
	}

	private Method extractCorrespondingBeanMethod(final String methodName, final Class<?> implClass) {
		Method beanMethod = null;
		for (final Method implMethod : implClass.getMethods()) {
			if (methodName.equals(implMethod.getName())) {
				beanMethod = implMethod;
				return beanMethod;
			}
		}
		return null;
	}

	private MatchingStrategy[] buildStrategy(final MatchingStrategy[] strategy) {
		return strategy;
	}

	private Class<?> extractReturnValueType(final Method m) {
		final Class<?> returnType = m.getReturnType();
		Assert.isTrue(returnType == ApplicationProtocol.class,
				"by convention, the return type has to be ApplicationProtocol");
		// ApplicationProtocol is a generic type
		final TypeVariable<?>[] typeParameters = returnType.getTypeParameters();
		if (typeParameters.length != 1) {
			final String message = "cannot retrieve type parameter of ApplicationProtocol, please check declaration";
			log.atError().log(message);
			throw new GenericDeclarationException(message);
		}
		return typeParameters[0].getClass();
	}

	/**
	 * call will fail, if the given interface is not annotated probably.
	 *
	 * @throws MissingAnnotationException if the annotation @Emulated is missing.
	 */
	private <T> void validateClassAnnotations(final Class<T> interfaceType) {
		final Emulated emulationAnnotation = interfaceType.getAnnotation(Emulated.class);
		if (emulationAnnotation == null) {
			final String message = "the interface has to be annotated as @Emulated";
			log.atError().log(message);
			throw new MissingAnnotationException(message);
		}
	}
}
