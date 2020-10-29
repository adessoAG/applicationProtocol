package de.adesso.example.framework.core;

import java.lang.reflect.ParameterizedType;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import de.adesso.example.framework.ApplicationAppendix;
import lombok.Getter;
import lombok.NonNull;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class AppendixRegistryImpl implements AppendixRegistry {

	private final List<String> appendixClassesNames;
	private Map<Class<Object>, Class<? extends ApplicationAppendix<?>>> map;
	private final ClassLoader classLoader;

	public AppendixRegistryImpl(final ClassLoader classLoader, final List<String> appendixClassesNames) {
		this.classLoader = classLoader;
		this.appendixClassesNames = appendixClassesNames;
		this.init();
	}

	public void init() {
		this.map = this.appendixClassesNames.stream()
				.peek(n -> log.atInfo().log("found appendix {}", n))
				.map(this::getClassFromClassName)
				.map(c -> new Mapping(this.getParameterType(c), c))
				.collect(Collectors.toMap(Mapping::getKey, Mapping::getValue));
		log.atInfo().log(
				"prepared all appendixes, if one is missing check @Appendix annotation, the class has to extend ApplicationAppendix and has to be independent");
	}

	@Override
	public Class<? extends ApplicationAppendix<?>> lookUp(@NonNull final Class<?> parameterType) {
		return this.map.get(parameterType);
	}

	@SuppressWarnings("unchecked")
	private Class<Object> getParameterType(final Class<? extends ApplicationAppendix<?>> appendixClass) {
		final ParameterizedType pt = (ParameterizedType) appendixClass.getGenericSuperclass();
		Class<Object> parameterTypeClass;
		try {
			final String typeName = pt.getActualTypeArguments()[0].getTypeName();
			parameterTypeClass = (Class<Object>) this.classLoader.loadClass(typeName);
		} catch (final ClassNotFoundException e) {
			final String message = String.format("could not load class %s, %s", e);
			log.error(message);
			throw new RuntimeException(message, e);
		}
		return parameterTypeClass;
	}

	@Getter
	private class Mapping {

		private final Class<Object> key;
		private final Class<? extends ApplicationAppendix<?>> value;

		public Mapping(final Class<Object> key, final Class<? extends ApplicationAppendix<?>> value) {
			this.key = key;
			this.value = value;
		}
	}

	@SuppressWarnings("unchecked")
	private Class<? extends ApplicationAppendix<?>> getClassFromClassName(final String className) {
		Class<?> loadedClass;
		try {
			loadedClass = this.classLoader.loadClass(className);
		} catch (final ClassNotFoundException e) {
			final String message = String.format("could not load class %s", className);
			log.atError().log(message);
			throw new RuntimeException(message, e);
		}
		if (ApplicationAppendix.class.isAssignableFrom(loadedClass)) {
			return (Class<? extends ApplicationAppendix<?>>) loadedClass;
		}
		final String message = String
				.format("incompatible class encountered, should be subclass of ApplicationAppendix: %s", className);
		log.atError().log(message);
		throw new RuntimeException(message);
	}
}
