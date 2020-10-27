package de.adesso.example.framework;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.PriorityOrdered;
import org.springframework.core.type.filter.AnnotationTypeFilter;

import de.adesso.example.framework.annotation.Appendix;
import de.adesso.example.framework.annotation.Emulated;
import lombok.extern.log4j.Log4j2;

@Configuration
@Log4j2
public class ApplicationBeanDefinitionRegistryPostProcessor
		implements BeanDefinitionRegistryPostProcessor, PriorityOrdered {

	private final static String basePackage = "de.adesso.example";

	private List<String> appendixClassesNames;

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
		return new AppendixRegistryImpl(this.appendixClassesNames);
	}

	private void prepareAppendixes(final BeanDefinitionRegistry registry) {
		/** register all appendixes to be able to initialize the AppendixRegistry */
		this.appendixClassesNames = findAppendixClasses(basePackage).stream()
				.map(BeanDefinition::getBeanClassName)
				.collect(Collectors.toList());
	}

	private void prepareEmulatedInterfaces(final BeanDefinitionRegistry registry) {
		for (final BeanDefinition beanDefintion : findEmulationInterfaces(basePackage)) {

			// register the bean
			final StringBuilder beanNameBuilder = firstToLower(beanDefintion.getBeanClassName());
			final String beanName = beanNameBuilder.toString();
			beanDefintion.setScope("singelton");
			beanDefintion.setFactoryBeanName(ApplicationProxyFactory.class.getName());
			log.atDebug().log("going to register bean: {}", beanDefintion.toString());
			registry.registerBeanDefinition(beanName, beanDefintion);

			// register the factory bean
//			final String factoryBeanName = beanNameBuilder.append("Factory").toString();
//			final RootBeanDefinition factoryBeanDefintion = new RootBeanDefinition(factoryBeanName);
//			factoryBeanDefintion.setBeanClass(ApplicationProxyFactory.class);
//			factoryBeanDefintion.setDescription("factory for bea " + beanName);
//			factoryBeanDefintion.setScope("singelton");
//			final ConstructorArgumentValues constructorArgumentValues = new ConstructorArgumentValues();
//			constructorArgumentValues.addGenericArgumentValue(beanName, "String");
//			factoryBeanDefintion.setConstructorArgumentValues(constructorArgumentValues);
//			registry.registerBeanDefinition(beanName, factoryBeanDefintion);
		}
	}

	@Override
	public int getOrder() {
		return 0;
	}

	private Set<BeanDefinition> findEmulationInterfaces(final String scanPackage) {
		final ClassPathScanningCandidateComponentProvider provider = createEmulationInterfaceScanner();
		final Set<BeanDefinition> annotatedInterfaces = provider.findCandidateComponents(scanPackage);

		return annotatedInterfaces;
	}

	private ClassPathScanningCandidateComponentProvider createEmulationInterfaceScanner() {
		// Don't pull default filters (@Component, etc.):
		final ClassPathScanningCandidateComponentProvider provider = new ApplicationClassPathEmulationInterfaceProvider();
		provider.addIncludeFilter(new AnnotationTypeFilter(Emulated.class));
		return provider;
	}

	private Set<BeanDefinition> findAppendixClasses(final String scanPackage) {
		final ClassPathScanningCandidateComponentProvider provider = createAppendixClassScanner();
		final Set<BeanDefinition> annotatedInterfaces = provider.findCandidateComponents(scanPackage);

		return annotatedInterfaces;
	}

	private ClassPathScanningCandidateComponentProvider createAppendixClassScanner() {
		// Don't pull default filters (@Component, etc.):
		final ClassPathScanningCandidateComponentProvider provider = new ApplicationClassPathAppendixProvider();
		provider.addIncludeFilter(new AnnotationTypeFilter(Appendix.class));
		return provider;
	}

	private StringBuilder firstToLower(final String name) {
		final StringBuilder sb = new StringBuilder(name);
		sb.setCharAt(0, Character.toLowerCase(sb.charAt(0)));
		return sb;
	}
}
