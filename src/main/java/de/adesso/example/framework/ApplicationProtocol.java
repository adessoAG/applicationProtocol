package de.adesso.example.framework;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import de.adesso.example.framework.exception.TooManyElementsException;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Basic protocol element for the application protocol. The type parameter
 * allows to add own return values of processing.
 * <p>
 * The protocol is prepared for single threaded use. If multi-threaded use is
 * required,
 *
 *
 * @author Matthias
 *
 */

@ToString
public class ApplicationProtocol<RESULT_TYPE> implements Serializable {

	private static final long serialVersionUID = 793704284936910537L;

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
	 * @param appendixClass the class of the requested appendix
	 * @return an optional containing of the requested type
	 * @throws TooManyElementsException if the list of appendixes contains more than
	 *                                  one element of type T
	 */
	public <T> Optional<T> getAppendixOfClassT(final Class<T> appendixClass) throws TooManyElementsException {
		return this.data.getAppendixOfTypeT(appendixClass);
	}

	/**
	 * Retrieves all appendixes of given type. The list may be empty if there is no
	 * such element in the list.
	 *
	 * @param appendixClass class of which the instances should be returned
	 * @return a list of all appendixes of the given type
	 */
	public <T> List<T> getAllAppenixesOfTypeAsListT(final Class<T> appendixClass) {
		return this.data.getAllAppenixesOfTypeAsListT(appendixClass);
	}

	/**
	 * Retrieves all appendixes of given type. The list may be empty if there is no
	 * such element in the list.
	 *
	 * @param appendixClass class of which the instances should be returned
	 * @return a set of all instances
	 */
	public <T> Set<T> getAllAppenixesOfTypeAsSetT(final Class<T> appendixClass) {
		return this.data.getAllAppenixesOfTypeAsSetT(appendixClass);
	}

	// modifier
	// -----------------------------------------------------------------------//

	/**
	 * the method adds an appendix
	 *
	 * @param additionalAppendix appendix to be added
	 * @return the protocol itself to allow fluent handling
	 */
	public <T> ApplicationProtocol<RESULT_TYPE> addAppendix(final ApplicationOwner owner, final T additionalAppendix) {
		this.data.addAppendix(owner, additionalAppendix);

		return this;
	}

	/**
	 * the method adds all appendixes to the protocol
	 *
	 * @param additionalAppendixes the appendixes to be added
	 * @return the protocol itself to allow fluent handling
	 */
	public ApplicationProtocol<RESULT_TYPE> addAllAppendixes(final ApplicationOwner owner,
			final Collection<?> additionalAppendixes) {
		this.data.addAppendix(owner, additionalAppendixes);

		return this;
	}

	/**
	 * the method adds all appendixes to the protocol
	 *
	 * @param additionalAppendixes the appendixes to be added
	 * @return the protocol itself to allow fluent handling
	 */
	public <T> ApplicationProtocol<RESULT_TYPE> addAllAppendixesT(final ApplicationOwner owner,
			final Collection<T> additionalAppendixes) {
		this.data.addAppendix(owner, additionalAppendixes);

		return this;
	}

	public <T> void removeAll(final ApplicationOwner owner, final Class<T> appendixType) {
		this.data.removeAll(owner, appendixType);
	}

	/**
	 * Transfer all appendixes from the other protocol to the own list. The
	 * appendixes are removed from the other protocol. The owner information remains
	 * unchanged.
	 *
	 * @param otherProtocol the protocol to hand over its appendixes
	 */
	public void transfertAppendixes(final ApplicationProtocol<?> otherProtocol) {
		this.data.transfer(otherProtocol.data);
	}
}
