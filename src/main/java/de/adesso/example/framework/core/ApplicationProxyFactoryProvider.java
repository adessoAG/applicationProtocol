package de.adesso.example.framework.core;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.context.ApplicationContext;

public class ApplicationProxyFactoryProvider implements FactoryBean<ApplicationProxyFactory> {

	private final Class<Object> emulatedInterface;
	private final ArgumentFactory argumentFactory;
	private final ApplicationContext context;

	public ApplicationProxyFactoryProvider(final ApplicationContext context, final ArgumentFactory argumentFactory,
			final Class<Object> emulatedInterface) {
		this.context = context;
		this.emulatedInterface = emulatedInterface;
		this.argumentFactory = argumentFactory;
	}

	@Override
	public ApplicationProxyFactory getObject() throws Exception {
		return new ApplicationProxyFactory(this.context, this.argumentFactory, this.emulatedInterface);
	}

	@Override
	public Class<ApplicationProxyFactory> getObjectType() {
		return ApplicationProxyFactory.class;
	}

}
