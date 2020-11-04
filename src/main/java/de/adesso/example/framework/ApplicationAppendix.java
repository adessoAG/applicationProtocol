package de.adesso.example.framework;

import java.util.UUID;

import lombok.Getter;

/**
 * Base type for the various appendixes. Every new type to be added to the
 * appendix list, should derive from this type. The properties are only
 * readable. This ensures, that they are only set during creation.
 *
 * @author Matthias
 *
 */
@Getter
public abstract class ApplicationAppendix<T> {

	/**
	 * content of the appendix
	 */
	@Getter
	private final T content;

	protected ApplicationAppendix(final T content) {
		this.content = content;
	}

	/**
	 * UUID of the owner which created this appendix.
	 */
	public abstract UUID getOwner();

	public String toString() {
		final StringBuilder sb = new StringBuilder();
		return this.toString(sb, 0).toString();
	}

	public StringBuilder toString(final StringBuilder sb, final int indent) {
		this.identation(sb, indent)
				.append(this.getClass().getName())
				.append("(owner = ").append(this.getOwner()).append("): ");
		sb.append(this.getContent().toString()).append('\n');
		return sb;
	}

	private StringBuilder identation(final StringBuilder sb, final int tabs) {
		for (int i = 0; i < tabs; i++) {
			sb.append('\t');
		}
		return sb;
	}
}
