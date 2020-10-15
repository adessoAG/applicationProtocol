package de.adesso.example.framework;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.springframework.stereotype.Service;

import lombok.NonNull;

@Service
public class AppendixRegistryImpl implements AppendixRegistry {

	private final Map<Class<?>, UUID> map = new HashMap<>();

	@Override
	public UUID lookUp(@NonNull final Class<?> parameterType) {
		return this.map.get(parameterType);
	}

	@Override
	public void register(@NonNull final UUID appendixId, @NonNull final Class<?> parameterType) {
		this.map.put(parameterType, appendixId);
	}
}
