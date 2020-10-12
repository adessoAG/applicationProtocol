package de.adesso.example.framework;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class AppendixList {

	/**
	 * List of all appendix values.
	 */
	public List<ApplicationAppendix> appendixes = new ArrayList<>();

	public AppendixList() {
	}

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
	public Object getAppendixOfType(final UUID appendixId) throws TooManyElementsException {
		final List<ApplicationAppendix> allAppendixesOfTypeT = getAllAppendixesOfTypeT(appendixId)
				.collect(Collectors.toList());
		if (allAppendixesOfTypeT.size() > 1) {
			throw new TooManyElementsException("more than one element");
		}
		if (allAppendixesOfTypeT.isEmpty()) {
			return null;
		}

		return allAppendixesOfTypeT.get(0);
	}

	/**
	 * Retrieves all appendixes of given type. The list may be empty if there is no
	 * such element in the list.
	 *
	 * @param <T>
	 * @param appendixId
	 * @return
	 */
	public <T> List<T> getAllAppenixesOfTypeAsList(final UUID appendixId) {
		@SuppressWarnings("unchecked")
		final List<T> allAppendixesOfTypeT = (List<T>) getAllAppendixesOfTypeT(appendixId)
				.collect(Collectors.toList());

		return allAppendixesOfTypeT;
	}

	/**
	 * Retrieves all appendixes of given type. The list may be empty if there is no
	 * such element in the list.
	 *
	 * @param <T>
	 * @param appendixId
	 * @return
	 */
	public <T> Set<T> getAllAppenixesOfTypeAsSet(final UUID appendixId) {
		@SuppressWarnings("unchecked")
		final Set<T> allAppendixesOfTypeT = (Set<T>) getAllAppendixesOfTypeT(appendixId)
				.collect(Collectors.toSet());

		return allAppendixesOfTypeT;
	}

	private Stream<ApplicationAppendix> getAllAppendixesOfTypeT(final UUID appendixId) {
		final Stream<ApplicationAppendix> stream = this.appendixes
				.stream()
				.filter(a -> a.getApplicationAppendixId() == appendixId);
		return stream;
	}

	public void addAppendix(final ApplicationAppendix additionalAppendix) {
		this.appendixes.add(additionalAppendix);
	}

	public void addAppendix(final Collection<ApplicationAppendix> additionalAppendixes) {
		this.appendixes.addAll(additionalAppendixes);
	}
}