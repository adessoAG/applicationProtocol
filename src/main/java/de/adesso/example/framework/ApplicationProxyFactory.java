package de.adesso.example.framework;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.TypeVariable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import de.adesso.example.framework.MethodImplementation.MethodImplementationBuilder;
import de.adesso.example.framework.annotation.Emulated;
import de.adesso.example.framework.annotation.Implementation;
import de.adesso.example.framework.annotation.MatchingStrategy;
import lombok.NonNull;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
@Scope("prototype")
public class ApplicationProxyFactory
		implements BeanFactoryAware, FactoryBean<Object>, SmartInitializingSingleton {

	private BeanFactory beanFactory;
	private Class<Object> emulatedInterface;
	private Object generatedEmulation;

	private ArgumentFactory argumentFactory;

	public ApplicationProxyFactory() {
	}

	@Override
	public void setBeanFactory(final BeanFactory beanFactory) throws BeansException {
		this.beanFactory = beanFactory;
	}

	@Override
	public Object getObject() {
		if (this.generatedEmulation == null) {
			this.generatedEmulation = emulateInterface(this.emulatedInterface);
		}
		return this.generatedEmulation;
	}

	@Override
	public Class<Object> getObjectType() {
		return this.emulatedInterface;
	}

	@Override
	public void afterSingletonsInstantiated() {
		// find the types to be emulated

		// register the types with the corresponding factory
	}

	private Object emulateInterface(@NonNull final Class<Object> interfaceType) {
		if (this.argumentFactory == null) {
			initializeArgumentFactory();
		}
		validateClassAnnotations(interfaceType);
		this.emulatedInterface = interfaceType;

		// initialize the factory to build the proxy
		final DaisyChainDispatcherFactory factory = new DaisyChainDispatcherFactory()
				.implementationInterface(interfaceType);

		// add the methods of the interface to be emulated
		processAllMethods(interfaceType).stream()
				.forEach(m -> factory.operation(m));

		return factory.build();
	}

	private void initializeArgumentFactory() {
		final AppendixRegistry registry = this.beanFactory.getBean(AppendixRegistry.class);
		this.argumentFactory = this.beanFactory.getBean(ArgumentFactory.class, registry);
	}

	private MethodImplementation buildMethodEmulation(final Method interfaceMethod) {
		Assert.notNull(interfaceMethod, "method is mandatory argument");

		final String methodName = interfaceMethod.getName();
		final MethodImplementationBuilder builder = MethodImplementation.builder()
				.methodIdentifier(methodName)
				.returnValueType(extractReturnValueType(interfaceMethod));

		// the interface may annotate several beans to provide implementations which
		// will be called consecutively
		final Implementation implAnnotation = interfaceMethod.getAnnotation(Implementation.class);
		final MatchingStrategy[] strategies = buildStrategy(implAnnotation.strategy());
		for (final Class<Object> implClass : implAnnotation.implementations()) {
			final Object implBean = this.beanFactory.getBean(implClass);
			BeanOperation.builder()
					.implementation((ApplicationFrameworkInvokable) implBean)
					.methodIdentifier(methodName);
			final Method beanMethod = extractCorrespondingBeanMethod(methodName, implClass);
			if (beanMethod == null) {
				// basic requirement: the implementation bean provides at least one method with
				// the same identifier
				continue;
			}

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
							argument = this.argumentFactory.createArgumentFromAppendix(interfaceMethod, p.getType());
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
					}
				}
				if (argument == null) {
					// could not find any content for the argument
					final String message = String.format("could not provide bean %s with argument % of type %s",
							implClass.getName(), p.getName(), p.getType());
					log.atFatal().log(message);
					throw new UndefinedParameterException(message);
				}
			}
		}
		return builder.build();
	}

	private Method extractCorrespondingBeanMethod(final String methodName, final Class<?> implClass) {
		Method beanMethod = null;
		for (final Method implMethod : implClass.getMethods()) {
			if (methodName.equals(implMethod.getName())) {
				beanMethod = implMethod;
				break;
			}
		}
		return beanMethod;
	}

	private MatchingStrategy[] buildStrategy(final MatchingStrategy[] strategy) {
		return strategy;
	}

	private Class<?> extractReturnValueType(final Method m) {
		final Class<?> returnType = m.getReturnType();
		Assert.isInstanceOf(ApplicationProtocol.class, "by convention, the return type has to be ApplicationProtocol");
		// ApplicationProtocol is a generic type
		final TypeVariable<?>[] typeParameters = returnType.getTypeParameters();
		if (typeParameters.length != 1) {
			final String message = "cannot retrieve type parameter of ApplicationProtocol, please check declaration";
			log.atError().log(message);
			throw new GenericDeclarationException(message);
		}
		return typeParameters[0].getClass();
	}

	private <T> Collection<MethodImplementation> processAllMethods(final Class<T> interfaceType) {
		final List<MethodImplementation> implementations = new ArrayList<>();
		for (final Method m : interfaceType.getMethods()) {
			final MethodImplementation methodEmulation = buildMethodEmulation(m);
			methodEmulation.method(m);
			implementations.add(methodEmulation);
		}
		return implementations;
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
