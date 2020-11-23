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
	 * @param appendixClass the type to be searched within the appendix
	 * @return an optional containing the probably found element
	 * @throws TooManyElementsException if the list of appendixes contains more than
	 *                                  one element of type T
	 */
	public <T> Optional<ApplicationAppendix<T>> getAppendixOfClassT(
			final Class<? extends ApplicationAppendix<T>> appendixClass) throws TooManyElementsException {
		return this.data.getAppendixOfTypeT(appendixClass);
	}

	/**
	 * Retrieves exactly one appendix of given type. If the list contains more than
	 * one appendix of this type, a {@link TooManyElementsException} is thrown. If
	 * the type T does not match to your local variable the runtime will throw a
	 * {@link ClassCastException}.
	 *
	 * @param appendixClass the type to be searched within the appendix
	 * @return an optional containing the probably found element
	 * @throws TooManyElementsException if the list of appendixes contains more than
	 *                                  one element of type T
	 */
	public Optional<ApplicationAppendix<?>> getAppendixOfClass(
			final Class<? extends ApplicationAppendix<?>> appendixClass) throws TooManyElementsException {
		return this.data.getAppendixOfType(appendixClass);
	}

	/**
	 * Retrieves all appendixes. The list may be empty if there is no such element
	 * in the list.
	 *
	 * @return a list of all appendixes of the given type
	 */
	public List<ApplicationAppendix<?>> getAllAppenixesAsList() {
		return this.data.getAllAppenixesAsList();
	}

	/**
	 * Retrieves all appendixes of given type. The list may be empty if there is no
	 * such element in the list.
	 *
	 * @param appendixClass class of which the instances should be returned
	 * @return a list of all appendixes of the given type
	 */
	public List<ApplicationAppendix<?>> getAllAppenixesOfTypeAsList(
			final Class<? extends ApplicationAppendix<?>> appendixClass) {
		return this.data.getAllAppenixesOfTypeAsList(appendixClass);
	}

	/**
	 * Retrieves all appendixes of given type. The list may be empty if there is no
	 * such element in the list.
	 *
	 * @param appendixClass class of which the instances should be returned
	 * @return a list of all appendixes of the given type
	 */
	public <T> List<ApplicationAppendix<T>> getAllAppenixesOfTypeAsListT(
			final Class<? extends ApplicationAppendix<T>> appendixClass) {
		return this.data.getAllAppenixesOfTypeAsListT(appendixClass);
	}

	/**
	 * Retrieves all appendixes of given type. The list may be empty if there is no
	 * such element in the list.
	 *
	 * @param appendixClass class of which the instances should be returned
	 * @return a set of all instances
	 */
	public Set<ApplicationAppendix<?>> getAllAppenixesOfTypeAsSet(
			final Class<? extends ApplicationAppendix<?>> appendixClass) {
		return this.data.getAllAppenixesOfTypeAsSet(appendixClass);
	}

	/**
	 * Retrieves all appendixes of given type. The list may be empty if there is no
	 * such element in the list.
	 *
	 * @param appendixClass class of which the instances should be returned
	 * @return a set of all instances
	 */
	public <T> Set<ApplicationAppendix<T>> getAllAppenixesOfTypeAsSetT(
			final Class<? extends ApplicationAppendix<T>> appendixClass) {
		return this.data.getAllAppenixesOfTypeAsSetT(appendixClass);
	}

	/**
	 * The class will return a list of all appendixes of the given owner
	 *
	 * @param owner the owner of appendixes
	 * @return the list of appendixes
	 */
	public List<ApplicationAppendix<?>> getAllAppendixesOfOwnerAsList(final ApplicationOwner owner) {
		return this.data.getAllAppendixesOfOwnerAsList(owner);
	}

	// modifier
	// -----------------------------------------------------------------------//

	/**
	 * the method adds an appendix
	 *
	 * @param additionalAppendix appendix to be added
	 * @return the protocol itself to allow fluent handling
	 */
	public ApplicationProtocol<RESULT_TYPE> addAppendix(final ApplicationAppendix<?> additionalAppendix) {
		this.data.addAppendix(additionalAppendix);

		return this;
	}

	/**
	 * the method adds all appendixes to the protocol
	 *
	 * @param additionalAppendixes the appendixes to be added
	 * @return the protocol itself to allow fluent handling
	 */
	public ApplicationProtocol<RESULT_TYPE> addAllAppendixes(
			final Collection<ApplicationAppendix<?>> additionalAppendixes) {
		this.data.addAppendix(additionalAppendixes);

		return this;
	}

	/**
	 * the method adds all appendixes to the protocol
	 *
	 * @param additionalAppendixes the appendixes to be added
	 * @return the protocol itself to allow fluent handling
	 */
	public <T> ApplicationProtocol<RESULT_TYPE> addAllAppendixesT(
			final Collection<ApplicationAppendix<T>> additionalAppendixes) {
		additionalAppendixes.stream().forEach(this.data::addAppendix);

		return this;
	}

	public <T> void removeAll(final Class<? extends ApplicationAppendix<T>> appendixType) {
		this.data.removeAll(appendixType);
	}
}
