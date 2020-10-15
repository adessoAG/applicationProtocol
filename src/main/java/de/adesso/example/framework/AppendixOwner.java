package de.adesso.example.framework;

import java.util.UUID;

import lombok.Getter;

public abstract class AppendixOwner {

	@Getter
	private final UUID ownUuid;

	public AppendixOwner(final UUID ownUuid) {
		this.ownUuid = ownUuid;
	}
}
