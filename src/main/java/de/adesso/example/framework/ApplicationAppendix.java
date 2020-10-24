package de.adesso.example.framework;

import java.util.UUID;

import lombok.Getter;
import lombok.ToString;

/**
 * Base type for the various appendixes. Every new type to be added to the
 * appendix list, should derive from this type. The properties are only
 * readable. This ensures, that they are only set during creation.
 *
 * @author Matthias
 *
 */
@Getter
@ToString
public abstract class ApplicationAppendix<T> {

	/**
	 * content of the appendix
	 */
	@Getter
	private final T content;

	private static Class<Object> contentType = null;

	protected ApplicationAppendix(final T content) {
		this.content = content;
	}

	/**
	 * UUID of the owner which created this appendix.
	 */
	public abstract UUID getOwner();

	/**
	 * Identifier for the type of appendix
	 */
	public abstract UUID getAppendixId();

	public static Class<Object> getType() {
		return ApplicationAppendix.contentType;
	}

	static void setType(final Class<Object> type) {
		ApplicationAppendix.contentType = type;
	}
}
