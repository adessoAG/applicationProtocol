package de.adesso.example.framework;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.ConstructorArgumentValues;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.PriorityOrdered;
import org.springframework.core.type.filter.AnnotationTypeFilter;

import de.adesso.example.framework.annotation.Appendix;
import de.adesso.example.framework.annotation.Emulated;

@Configuration
public class ApplicationBeanDefinitionRegistryPostProcessor
		implements BeanDefinitionRegistryPostProcessor, PriorityOrdered {

	private final static String basePackage = "de.adesso.example";

	private final List<String> appendixClassNames = new ArrayList<>();

	@Override
	public void postProcessBeanFactory(final ConfigurableListableBeanFactory beanFactory) throws BeansException {
	}

	@Override
	public void postProcessBeanDefinitionRegistry(final BeanDefinitionRegistry registry) throws BeansException {
		prepareAppendixes(registry);
		prepareEmulatedInterfaces(registry);
	}

	@Bean
	public AppendixRegistry appendixRegistry() {
		return new AppendixRegistryImpl(this.appendixClassNames);
	}

	private void prepareAppendixes(final BeanDefinitionRegistry registry) {
		/** register all appendixes to be able to initialize the AppendixRegistry */
		for (final BeanDefinition beanDefinition : findAnnotatedClasses(basePackage, Appendix.class)) {
			beanDefinition.setScope("prototype");
			registry.registerBeanDefinition(beanDefinition.getBeanClassName(), beanDefinition);
		}
	}

	private void prepareEmulatedInterfaces(final BeanDefinitionRegistry registry) {
		for (final BeanDefinition beanDefintion : findAnnotatedClasses(basePackage, Emulated.class)) {

			// register the bean
			final StringBuilder beanNameBuilder = firstToLower(beanDefintion.getBeanClassName());
			final String beanName = beanNameBuilder.toString();
			beanDefintion.setScope("singelton");
			registry.registerBeanDefinition(beanName, beanDefintion);

			// register the factory bean
			final String factoryBeanName = beanNameBuilder.append("Factory").toString();
			final RootBeanDefinition factoryBeanDefintion = new RootBeanDefinition(factoryBeanName);
			factoryBeanDefintion.setBeanClass(ApplicationProxyFactory.class);
			factoryBeanDefintion.setDescription("factory for bea " + beanName);
			factoryBeanDefintion.setScope("singelton");
			final ConstructorArgumentValues constructorArgumentValues = new ConstructorArgumentValues();
			constructorArgumentValues.addGenericArgumentValue(beanName, "String");
			factoryBeanDefintion.setConstructorArgumentValues(constructorArgumentValues);
			registry.registerBeanDefinition(beanName, factoryBeanDefintion);
		}
	}

	@Override
	public int getOrder() {
		return 0;
	}

	private Set<BeanDefinition> findAnnotatedClasses(final String scanPackage,
			final Class<? extends Annotation> annotation) {
		final ClassPathScanningCandidateComponentProvider provider = createComponentScanner(annotation);
		final Set<BeanDefinition> annotatedInterfaces = provider.findCandidateComponents(scanPackage);
		for (final BeanDefinition beanDef : annotatedInterfaces) {
			printMetadata(beanDef);
		}
		return annotatedInterfaces;
	}

	private ClassPathScanningCandidateComponentProvider createComponentScanner(
			final Class<? extends Annotation> annotation) {
		// Don't pull default filters (@Component, etc.):
		final ClassPathScanningCandidateComponentProvider provider = new ApplicationClassPathEmulationInterfaceProvider();
		provider.addIncludeFilter(new AnnotationTypeFilter(annotation));
		return provider;
	}

	private void printMetadata(final BeanDefinition beanDef) {
		try {
			final Class<?> cl = Class.forName(beanDef.getBeanClassName());
			final Emulated emulated = cl.getAnnotation(Emulated.class);
			System.out.printf("Found class: %s annoted with %s", cl.getSimpleName(), emulated.getClass().getName());
		} catch (final Exception e) {
			System.err.println("Got exception: " + e.getMessage());
		}
	}

	private StringBuilder firstToLower(final String name) {
		final StringBuilder sb = new StringBuilder(name);
		sb.setCharAt(0, Character.toLowerCase(sb.charAt(0)));
		return sb;
	}
}
