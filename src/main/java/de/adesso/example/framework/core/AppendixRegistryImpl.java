package de.adesso.example.framework.core;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import de.adesso.example.framework.ApplicationAppendix;
import lombok.NonNull;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class AppendixRegistryImpl implements AppendixRegistry {

	private final Map<Class<Object>, Class<? extends ApplicationAppendix<?>>> map;

	public AppendixRegistryImpl(final List<Class<? extends ApplicationAppendix<?>>> appendixClasses) {
		this.map = appendixClasses.stream()
				.peek(c -> log.atInfo().log("found appendix {}", c.getName()))
				.collect(Collectors.toMap(ApplicationAppendix::getParameterType, c -> c));
		log.atInfo().log(
				"prepared all appendixes, if one is missing check @Appendix annotation, the class has to extend ApplicationAppendix and has to be independent");
	}

	@Override
	public Class<? extends ApplicationAppendix<?>> lookUp(@NonNull final Class<?> parameterType) {
		return this.map.get(parameterType);
	}
}
