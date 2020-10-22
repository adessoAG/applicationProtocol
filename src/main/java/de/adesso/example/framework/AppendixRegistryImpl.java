package de.adesso.example.framework;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import lombok.NonNull;

public class AppendixRegistryImpl implements AppendixRegistry, ApplicationContextAware {

	private Map<Class<?>, UUID> map;
	private final List<String> appendixClassNames;
	private ApplicationContext applicationContext;

	public AppendixRegistryImpl(final List<String> appendixClassNames) {
		this.appendixClassNames = appendixClassNames;
	}

	@Override
	public void setApplicationContext(final ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}

	@PostConstruct
	public void init() {
		this.appendixClassNames.stream()
				.map(name -> (TApplicationAppendix<?>) this.applicationContext.getBean(name))
				.collect(Collectors.toMap(TApplicationAppendix::getType,
						TApplicationAppendix::getApplicationAppendixId));
	}

	@Override
	public UUID lookUp(@NonNull final Class<?> parameterType) {
		return this.map.get(parameterType);
	}

	@Override
	public void register(@NonNull final UUID appendixId, @NonNull final Class<?> parameterType) {
		this.map.put(parameterType, appendixId);
	}
}
