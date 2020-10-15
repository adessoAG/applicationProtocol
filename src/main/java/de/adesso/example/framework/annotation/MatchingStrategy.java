package de.adesso.example.framework.annotation;

public enum MatchingStrategy {
	/**
	 * Identifiers of parameters of emulated interface and implementing bean must
	 * match.
	 */
	ByType,
	/**
	 * Types of parameters of emulated interface and implementing bean must match.
	 */
	ByName,
	/**
	 * The type of a parameter within the emulated interface is found within the
	 * appendix
	 */
	FromAppendix
}
