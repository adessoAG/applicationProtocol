package de.adesso.example.framework.core;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.ConstructorArgumentValues;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.PriorityOrdered;
import org.springframework.core.type.filter.AnnotationTypeFilter;

import de.adesso.example.framework.ApplicationAppendix;
import de.adesso.example.framework.annotation.Appendix;
import de.adesso.example.framework.annotation.Emulated;
import de.adesso.example.framework.exception.BuilderException;
import lombok.extern.log4j.Log4j2;

/**
 * This class is the anchor for the application framework if it is working with
 * annotations. The {@link BeanDefinitionRegistryPostProcessor} is called by
 * spring before the beans are instantiated. Therefore this class can do early
 * initialization work. It implies also, that this class does not instantiate
 * the beans. This is task of the container.
 * <p>
 * The method
 * {@link ApplicationBeanDefinitionRegistryPostProcessor#postProcessBeanFactory}
 * performs the initialization. First it scans the paths maintained by Spring
 * for appendixes, thus classes annotated with the annotation @Appendix. These
 * classes are required to initialize the AppendixRegistry.
 * <p>
 * The next task is the lookup of interfaces which need to be emulated. For all
 * of these interfaces a factory and a bean is created.
 *
 * @author Matthias
 *
 */
@Configuration
@Log4j2
public class ApplicationBeanDefinitionRegistryPostProcessor
		implements BeanDefinitionRegistryPostProcessor, PriorityOrdered, ApplicationContextAware {

	private final static String basePackage = "de.adesso.example";

	private List<Class<? extends ApplicationAppendix<?>>> appendixClasses = null;
	private ApplicationContext applicationContext;
	private ArgumentFactory argumentFactorySingelton = null;
	private AppendixRegistry appendixRegistrySingelton;

	@Override
	public void postProcessBeanFactory(final ConfigurableListableBeanFactory beanFactory) throws BeansException {
	}

	@Override
	public void postProcessBeanDefinitionRegistry(final BeanDefinitionRegistry registry) throws BeansException {
		this.prepareAppendixes(registry);
		this.prepareEmulatedInterfaces(registry);
	}

	@Override
	public void setApplicationContext(final ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}

	/**
	 * Run early to add further beans to the registry. Thus the order returned is 0;
	 */
	@Override
	public int getOrder() {
		return HIGHEST_PRECEDENCE;
	}

	@Bean
	public AppendixRegistry appendixRegistry() {
		if (this.appendixRegistrySingelton == null) {
			this.appendixRegistrySingelton = new AppendixRegistryImpl(this.applicationContext.getClassLoader(),
					this.appendixClasses);
		}
		return this.appendixRegistrySingelton;
	}

	@Bean
	public ArgumentFactory argumentFactory() {
		if (this.argumentFactorySingelton == null) {
			this.argumentFactorySingelton = new ArgumentFactory(this.appendixRegistry());
		}
		return this.argumentFactorySingelton;
	}

	private void prepareAppendixes(final BeanDefinitionRegistry registry) {
		/** register all appendixes to be able to initialize the AppendixRegistry */
		this.appendixClasses = this.findAppendixClasses(basePackage).stream()
				.peek(bd -> this.check(registry, bd))
				.map(this::getClassFromClassName)
				.collect(Collectors.toList());
		log.atInfo().log("found the following appendix classes: {}", this.appendixClasses.toString());
	}

	@SuppressWarnings("unchecked")
	private Class<? extends ApplicationAppendix<Object>> getClassFromClassName(final BeanDefinition beanDefinition) {
		Class<?> clazz;

		// load the class
		final String className = beanDefinition.getBeanClassName();
		try {
			clazz = this.applicationContext.getClassLoader().loadClass(className);
		} catch (final ClassNotFoundException e) {
			final String message = String.format("could not load class %s", className);
			log.atError().log(message);
			throw new RuntimeException(message, e);
		}

		this.validateAppendixClass(clazz);

		return (Class<? extends ApplicationAppendix<Object>>) clazz;
	}

	private void validateAppendixClass(final Class<?> beanClass) {
		if (!ApplicationAppendix.class.isAssignableFrom(beanClass)) {
			// wrong implementation of class
			final String message = String.format(
					"Appendixes are required to subclass ApplicationAppendix, please check class %s",
					beanClass.getName());
			log.atError().log(message);
			throw new BuilderException(message);
		}
	}

	private BeanDefinition check(final BeanDefinitionRegistry registry, final BeanDefinition bd) {
		final String beanName = this.firstToLower(bd.getBeanClassName()).toString();
		try {
			final BeanDefinition beanDefinition = registry.getBeanDefinition(beanName);
			if (beanDefinition != null) {
				log.atWarn().log("bean {} already defined. Going to remove it from registry.", beanName);
				registry.registerBeanDefinition(beanName, beanDefinition);
			}
		} catch (final NoSuchBeanDefinitionException e) {
			// ignore
		}
		return bd;
	}

	@SuppressWarnings("unchecked")
	private void prepareEmulatedInterfaces(final BeanDefinitionRegistry registry) {

		for (final BeanDefinition beanDefintion : this.findEmulationInterfaces(basePackage)) {
			final StringBuilder beanNameBuilder = this.firstToLower(beanDefintion.getBeanClassName());
			final String beanName = beanNameBuilder.toString();
			final String factoryBeanName = beanNameBuilder.append("Factory").toString();
			Class<Object> emulatedInterface;
			try {
				emulatedInterface = (Class<Object>) this.applicationContext.getClassLoader()
						.loadClass(beanDefintion.getBeanClassName());
			} catch (final ClassNotFoundException e) {
				final String message = String
						.format("could not load class of emulated interface, please check spelling (%s)", beanName);
				throw new BuilderException(message, e);
			}

			// factory definition
			final RootBeanDefinition factoryBeanDefintion = this.buildFactoryDefinition(
					emulatedInterface,
					factoryBeanName);

			this.registerBeanIfNotAlreadyRegistered(registry, factoryBeanName, factoryBeanDefintion);
		}
	}

	private RootBeanDefinition buildFactoryDefinition(
			final Class<Object> emulatedInterface,
			final String factoryBeanName) {

		final RootBeanDefinition factoryBeanDefintion = new RootBeanDefinition();

		factoryBeanDefintion.setAutowireMode(RootBeanDefinition.AUTOWIRE_BY_NAME);
		factoryBeanDefintion.setBeanClass(ApplicationProxyFactory.class);
		factoryBeanDefintion.setConstructorArgumentValues(
				this.buildFactoryConstructorArguments(emulatedInterface));
		factoryBeanDefintion.setDescription("factory for bean " + emulatedInterface.getName());
		factoryBeanDefintion.setRole(BeanDefinition.ROLE_INFRASTRUCTURE);
		factoryBeanDefintion.setScope(ConfigurableBeanFactory.SCOPE_SINGLETON);
		factoryBeanDefintion.setTargetType(emulatedInterface);

		return factoryBeanDefintion;
	}

	private ConstructorArgumentValues buildFactoryConstructorArguments(final Class<Object> emulatedInterface) {
		final ConstructorArgumentValues constructorArgumentValues = new ConstructorArgumentValues();
		constructorArgumentValues.addGenericArgumentValue(this.argumentFactory(), "ArgumentFactory");
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
		final ClassPathScanningCandidateComponentProvider provider = this.createEmulationInterfaceScanner();
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
		final ClassPathScanningCandidateComponentProvider provider = this.createAppendixClassScanner();
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
