package de.adesso.example.framework;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.TypeVariable;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AbstractFactoryBean;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import de.adesso.example.framework.MethodImplementation.MethodImplementationBuilder;
import de.adesso.example.framework.annotation.Emulated;
import de.adesso.example.framework.annotation.Implementation;
import de.adesso.example.framework.annotation.MatchingStrategy;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
@Scope("prototype")
public class ApplicationFactory<T> extends AbstractFactoryBean<T> implements FactoryBean<T> {

	private final Class<T> emulatedInterface;
	private T singelton;

	@Autowired
	ArgumentFactory argumentFactory;

	public ApplicationFactory(final Class<T> emulatedInterface) {
		this.emulatedInterface = emulatedInterface;
	}

	@Override
	protected T createInstance() throws Exception {
		if (this.singelton != null) {
			return this.singelton;
		}

		validateClassAnnotations();

		final DaisyChainDispatcherFactory factory = new DaisyChainDispatcherFactory()
				.implementationInterface(this.emulatedInterface);
		processAllMethods().stream()
				.forEach(m -> factory.operation(m));

		final T implementedInterface = factory.build();
		this.singelton = implementedInterface;

		return implementedInterface;
	}

	@Override
	public Class<T> getObjectType() {
		return this.emulatedInterface;
	}

	private MethodImplementation buildMethodEmulation(final Method m) {
		Assert.notNull(m, "method is mandatory argument");

		final String methodName = m.getName();
		final MethodImplementationBuilder builder = MethodImplementation.builder()
				.methodIdentifier(methodName)
				.returnValueType(extractReturnValueType(m));

		final Implementation implAnnotation = m.getAnnotation(Implementation.class);
		final MatchingStrategy[] strategies = buildStrategy(implAnnotation.strategy());
		for (final Class<?> implClass : implAnnotation.implementations()) {
			Method target = null;
			for (final Method implMethod : implClass.getMethods()) {
				if (implMethod.getName().equals(m.getName())) {
					target = implMethod;
					break;
				}
			}
			// found appropriate method, now instrument it
			for (final Parameter p : target.getParameters()) {
				// find a provider for each parameter
				for (final MatchingStrategy strategy : strategies) {
					switch (strategy) {
					case ByType:
						ArgumentFactory.
						break;
					case ByName:
						break;
					case FromAppendix:
						break;
					default:
						// should not be reached
						throw new RuntimeException("missing implementation");
					}
				}
			}
		}
		return builder.build();
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

	private List<MethodImplementation> processAllMethods() {
		final List<MethodImplementation> implementations = new ArrayList<>();
		for (final Method m : this.emulatedInterface.getMethods()) {
			implementations.add(buildMethodEmulation(m));
		}
		return implementations;
	}

	/**
	 * call will fail, if the given interface is not annotated probably.
	 *
	 * @throws MissingAnnotationException if the annotation @Emulated is missing.
	 */
	private void validateClassAnnotations() {
		final Emulated emulationAnnotation = this.emulatedInterface.getAnnotation(Emulated.class);
		if (emulationAnnotation == null) {
			final String message = "the interface has to be annotated as @Emulated";
			log.atError().log(message);
			throw new MissingAnnotationException(message);
		}
	}
}
