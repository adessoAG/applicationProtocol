package de.adesso.example.framework;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import de.adesso.example.framework.exception.TooManyElementsException;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Basic protocol element for the application protocol. Users should extend this
 * class to add their own attributes.
 *
 * @author Matthias
 *
 */

@ToString
public class ApplicationProtocol<RESULT_TYPE> {

	private final AppendixList data = new AppendixList();

	/**
	 * The result value, which is provided by the stream.
	 */
	@Getter
	@Setter
	private RESULT_TYPE result;

	/**
	 * Retrieves exactly one appendix of given type. If the list contains more than
	 * one appendix of this type, a {@link TooManyElementsException} is thrown. If
	 * the type T does not match to your local variable the runtime will throw a
	 * {@link ClassCastException}.
	 *
	 * @param <T>
	 * @param appendixId
	 * @return
	 * @throws TooManyElementsException if the list of appendixes contains more than
	 *                                  one element of type T
	 */
	public Optional<ApplicationAppendix<?>> getAppendixOfType(final UUID appendixId) throws TooManyElementsException {
		return this.data.getAppendixOfType(appendixId);
	}

	/**
	 * Retrieves all appendixes of given type. The list may be empty if there is no
	 * such element in the list.
	 *
	 * @param <T>
	 * @param appendixId
	 * @return
	 */
	public List<ApplicationAppendix<?>> getAllAppenixesOfTypeAsList(final UUID appendixId) {
		return this.data.getAllAppenixesOfTypeAsList(appendixId);
	}

	/**
	 * Retrieves all appendixes of given type. The list may be empty if there is no
	 * such element in the list.
	 *
	 * @param <T>
	 * @param appendixId
	 * @return
	 */
	public Set<ApplicationAppendix<?>> getAllAppenixesOfTypeAsSet(final UUID appendixId) {
		return this.data.getAllAppenixesOfTypeAsSet(appendixId);
	}

	// modifier
	// -----------------------------------------------------------------------//

	public ApplicationProtocol<RESULT_TYPE> addAppendix(final ApplicationAppendix<?> additionalAppendix) {
		this.data.addAppendix(additionalAppendix);

		return this;
	}

	public ApplicationProtocol<RESULT_TYPE> addAllAppendixes(
			final Collection<ApplicationAppendix<?>> additionalAppendixes) {
		this.data.addAppendix(additionalAppendixes);

		return this;
	}
}
