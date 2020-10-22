package de.adesso.example.framework;

import java.util.UUID;

import lombok.Getter;
import lombok.ToString;

/**
 * Base type for the various appendixes. Every new type to be added to the
 * appendix list, should derive from this type. The properties are only readable. 
 * This ensures, that they are only set during creation. 
 * 
 * @author Matthias
 *
 */
@Getter
@ToString
public abstract class ApplicationAppendix {
	/**
	 * Identifier for the type of appendix
	 */
	private UUID applicationAppendixId;
	/**
	 * UUID of the owner which created this appendix. 
	 */
	private UUID owner;
	
	public ApplicationAppendix(UUID ownerId, UUID applicationAppendixId) {
		this.applicationAppendixId = applicationAppendixId;
		this.owner = ownerId;
	}
}
