package de.adesso.example.framework.core;

import de.adesso.example.framework.ApplicationAppendix;
import lombok.NonNull;

/**
 * The class maintains an overview over all appendixes. Since the appendixes are
 * looked up by the type they contain, only one appendix per type is allowed.
 *
 * @author Matthias
 *
 */
interface AppendixRegistry {

	/**
	 * Find an id of an appendix providing the required type.
	 *
	 * @param parameterType the required type
	 * @return the UUID of the appendix providing this type
	 */
	Class<? extends ApplicationAppendix<?>> lookUp(@NonNull Class<?> parameterType);
}
