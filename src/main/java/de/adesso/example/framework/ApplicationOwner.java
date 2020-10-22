package de.adesso.example.framework;

import java.util.UUID;

import lombok.Getter;

public abstract class ApplicationOwner {

	@Getter
	private final UUID ownUuid;

	public ApplicationOwner(final UUID ownUuid) {
		this.ownUuid = ownUuid;
	}
}
