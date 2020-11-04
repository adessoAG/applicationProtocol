package de.adesso.example.framework.core;

import java.lang.reflect.ParameterizedType;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import de.adesso.example.framework.ApplicationAppendix;
import lombok.NonNull;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class AppendixRegistryImpl implements AppendixRegistry {

	private final Map<Class<Object>, Class<? extends ApplicationAppendix<?>>> map;
	private final ClassLoader classLoader;

	public AppendixRegistryImpl(final ClassLoader classLoader,
			final List<Class<? extends ApplicationAppendix<?>>> appendixClasses) {
		this.classLoader = classLoader;
		this.map = appendixClasses.stream()
				.peek(c -> log.atInfo().log("found appendix {}", c.getName()))
				.collect(Collectors.toMap(this::getParameterType, c -> c));
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
}
