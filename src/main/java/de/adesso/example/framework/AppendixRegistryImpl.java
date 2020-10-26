package de.adesso.example.framework;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.BeanClassLoaderAware;

import lombok.Getter;
import lombok.NonNull;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class AppendixRegistryImpl implements AppendixRegistry, BeanClassLoaderAware {

	private final Map<Class<Object>, Class<? extends ApplicationAppendix<?>>> map;
	private ClassLoader classLoader;

	public AppendixRegistryImpl(final List<Class<?>> appendixClasses) {
		this.map = null;
		appendixClasses.stream()
				.peek(c -> log.atInfo().log("found appendix %s", c.getName()))
				.map(this::convertType)
				.map(c -> new Mapping(getParameterType(c), c))
				.peek(this::setTypeTo)
				.collect(Collectors.toMap(Mapping::getKey, Mapping::getValue));
		log.atInfo().log(
				"prepared all appendixes, if one is missing check @Appendix annotation, the class has to extend ApplicationAppendix and has to be independent");
	}

	@Override
	public void setBeanClassLoader(final ClassLoader classLoader) {
		this.classLoader = classLoader;
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

	/**
	 * The cast is possible without any risk, because type was already checked by
	 * the {@link ApplicationClassPathAppendixProvider}.
	 *
	 * @param c the class to be casted
	 * @return the casted object
	 */
	@SuppressWarnings("unchecked")
	private Class<? extends ApplicationAppendix<?>> convertType(final Class<?> c) {
		return (Class<? extends ApplicationAppendix<?>>) c;
	}

	private Mapping setTypeTo(final Mapping mapping) {
		Method m;
		try {
			m = mapping.value.getMethod("setType", Class.class);
			m.invoke(null, this.map.get(mapping.key));
		} catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException
				| InvocationTargetException e) {
			final String message = "could not set the type to the appendix class";
			log.atError().log(message);
			throw new RuntimeException(message, e);
		}

		return mapping;
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
}
