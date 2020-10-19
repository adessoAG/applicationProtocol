package de.adesso.example.framework;

import java.util.UUID;

import lombok.Getter;

public class TApplicationAppendix<T> extends ApplicationAppendix {

	@Getter
	private final T content;

	public TApplicationAppendix(final UUID applicationAppendixId, final UUID owner, final T content) {
		super(applicationAppendixId, owner);
		this.content = content;
	}
}
