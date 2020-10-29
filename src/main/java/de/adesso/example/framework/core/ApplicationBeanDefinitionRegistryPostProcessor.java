package de.adesso.example.framework.core;

import java.util.List;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanClassLoaderAware;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.ConstructorArgumentValues;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.PriorityOrdered;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.util.Assert;

import de.adesso.example.framework.annotation.Appendix;
import de.adesso.example.framework.annotation.Emulated;
import lombok.extern.log4j.Log4j2;

@Configuration
@Log4j2
public class ApplicationBeanDefinitionRegistryPostProcessor
		implements BeanDefinitionRegistryPostProcessor, PriorityOrdered, BeanClassLoaderAware, ApplicationContextAware {

	private final static String basePackage = "de.adesso.example";

	private List<String> appendixClassesNames = null;
	private ClassLoader classLoader;
	private AppendixRegistry appendixRegistry = null;
	private ArgumentFactory argumentFactory = null;

	private ApplicationContext context;

	@Override
	public void postProcessBeanFactory(final ConfigurableListableBeanFactory beanFactory) throws BeansException {
	}

	@Override
	public void postProcessBeanDefinitionRegistry(final BeanDefinitionRegistry registry) throws BeansException {
		prepareAppendixes(registry);
		prepareEmulatedInterfaces(registry);
	}

	@Override
	public void setApplicationContext(final ApplicationContext applicationContext) throws BeansException {
		this.context = applicationContext;
	}

	@Override
	public void setBeanClassLoader(final ClassLoader classLoader) {
		this.classLoader = classLoader;
	}

	private AppendixRegistry appendixRegistry() {
		Assert.notNull(this.appendixClassesNames, "out of order call, class names of appendixes not defined");
		if (this.appendixRegistry == null) {
			this.appendixRegistry = new AppendixRegistryImpl(this.classLoader, this.appendixClassesNames);
		}
		return this.appendixRegistry;
	}

	private ArgumentFactory argumentFactory() {
		if (this.argumentFactory == null) {
			this.argumentFactory = new ArgumentFactory(appendixRegistry());
		}
		return this.argumentFactory;
	}

	@Override
	public int getOrder() {
		return 0;
	}

	private void prepareAppendixes(final BeanDefinitionRegistry registry) {
		/** register all appendixes to be able to initialize the AppendixRegistry */
		this.appendixClassesNames = findAppendixClasses(basePackage).stream()
				.map(BeanDefinition::getBeanClassName)
				.collect(Collectors.toList());
	}

	@SuppressWarnings("unchecked")
	private void prepareEmulatedInterfaces(final BeanDefinitionRegistry registry) {
		for (final BeanDefinition beanDefintion : findEmulationInterfaces(basePackage)) {
			final StringBuilder beanNameBuilder = firstToLower(beanDefintion.getBeanClassName());
			final String beanName = beanNameBuilder.toString();
			final String factoryBeanName = beanNameBuilder.append("Factory").toString();
			Class<Object> emulatedInterface;
			try {
				emulatedInterface = (Class<Object>) this.classLoader.loadClass(beanDefintion.getBeanClassName());
			} catch (final ClassNotFoundException e) {
				final String message = String
						.format("could not load class of emulated interface, please check spelling (%s)", beanName);
				throw new RuntimeException(message, e);
			}

			// bean definition
			enrichBeanDefinition(beanDefintion, factoryBeanName);

			// factory definition
			final RootBeanDefinition factoryBeanDefintion = buildFactoryDefinition(beanDefintion, emulatedInterface,
					factoryBeanName);

			registerBeanIfNotAlreadyRegistered(registry, factoryBeanName, factoryBeanDefintion);
			registerBeanIfNotAlreadyRegistered(registry, beanName, beanDefintion);
		}
	}

	private void enrichBeanDefinition(final BeanDefinition beanDefintion, final String factoryBeanName) {
		final String description = "factory to create the emulated interface : " + beanDefintion.getBeanClassName();

		beanDefintion.setScope(ConfigurableBeanFactory.SCOPE_SINGLETON);
		beanDefintion.setAutowireCandidate(true);
		beanDefintion.setRole(BeanDefinition.ROLE_APPLICATION);
		beanDefintion.setDescription(description);
		beanDefintion.setFactoryBeanName(factoryBeanName);
		beanDefintion.setFactoryMethodName("getObject");
		beanDefintion.setInitMethodName("afterPropertiesSet");
	}

	private RootBeanDefinition buildFactoryDefinition(final BeanDefinition beanDefintion,
			final Class<Object> emulatedInterface,
			final String factoryBeanName) {
		final RootBeanDefinition factoryBeanDefintion = new RootBeanDefinition(factoryBeanName);

		factoryBeanDefintion.setBeanClass(ApplicationProxyFactoryProvider.class);
		factoryBeanDefintion.setTargetType(ApplicationProxyFactoryProvider.class);
		factoryBeanDefintion.setInstanceSupplier(new Supplier<>() {

			private final Class<Object> ef = emulatedInterface;

			@Override
			public ApplicationProxyFactoryProvider get() {
				return new ApplicationProxyFactoryProvider(
						ApplicationBeanDefinitionRegistryPostProcessor.this.context, argumentFactory(), this.ef);
			}
		});
		factoryBeanDefintion.setDescription("factory for bean " + emulatedInterface.getName());
		factoryBeanDefintion.setScope(ConfigurableBeanFactory.SCOPE_PROTOTYPE);
		beanDefintion.setRole(BeanDefinition.ROLE_INFRASTRUCTURE);
		factoryBeanDefintion
				.setConstructorArgumentValues(buildFactoryConstructorArguments(beanDefintion, emulatedInterface));
		factoryBeanDefintion.setFactoryMethodName("create");

		return factoryBeanDefintion;
	}

	private ConstructorArgumentValues buildFactoryConstructorArguments(final BeanDefinition beanDefintion,
			final Class<Object> emulatedInterface) {
		final ConstructorArgumentValues constructorArgumentValues = new ConstructorArgumentValues();
		constructorArgumentValues.addGenericArgumentValue(emulatedInterface, "Class");

		return constructorArgumentValues;
	}

	private void registerBeanIfNotAlreadyRegistered(final BeanDefinitionRegistry registry, final String beanName,
			final BeanDefinition beanDefintion) {
		if (!registry.containsBeanDefinition(beanName)) {
			log.atDebug().log("going to register bean: {}: {}", beanName, beanDefintion.toString());
			registry.registerBeanDefinition(beanName, beanDefintion);
		}
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
		final StringBuilder sb = new StringBuilder(name.substring(name.lastIndexOf('.') + 1, name.length()));
		sb.setCharAt(0, Character.toLowerCase(sb.charAt(0)));
		return sb;
	}
}
