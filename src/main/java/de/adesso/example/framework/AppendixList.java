package de.adesso.example.framework;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import de.adesso.example.framework.exception.TooManyElementsException;

class AppendixList {

	/**
	 * List of all appendix values.
	 */
	public List<ApplicationAppendix<?>> appendixes = new ArrayList<>();

	public AppendixList() {
	}

	/**
	 * Retrieves exactly one appendix of given type. If the list contains more than
	 * one appendix of this type, a {@link TooManyElementsException} is thrown. If
	 * the type T does not match to your local variable the runtime will throw a
	 * {@link ClassCastException}.
	 *
	 * @param <T>
	 * @param appendixClass
	 * @return
	 * @throws TooManyElementsException if the list of appendixes contains more than
	 *                                  one element of type T
	 */
	public Optional<ApplicationAppendix<?>> getAppendixOfType(
			final Class<? extends ApplicationAppendix<?>> appendixClass) throws TooManyElementsException {
		final List<? extends ApplicationAppendix<?>> allAppendixesOfTypeT = this.getAllAppendixesOfClass(appendixClass)
				.collect(Collectors.toList());
		if (allAppendixesOfTypeT.size() > 1) {
			throw new TooManyElementsException("more than one element");
		}
		if (allAppendixesOfTypeT.isEmpty()) {
			return Optional.empty();
		}

		return Optional.of(allAppendixesOfTypeT.get(0));
	}

	/**
	 * Retrieves all appendixes of given type. The list may be empty if there is no
	 * such element in the list.
	 *
	 * @param <T>
	 * @param appendixId
	 * @return
	 */
	public <T> List<T> getAllAppenixesOfTypeAsList(final Class<? extends ApplicationAppendix<?>> appendixClass) {
		@SuppressWarnings("unchecked")
		final List<T> allAppendixesOfTypeT = (List<T>) this.getAllAppendixesOfClass(appendixClass)
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
	public <T> Set<T> getAllAppenixesOfTypeAsSet(final Class<? extends ApplicationAppendix<?>> appendixClass) {
		@SuppressWarnings("unchecked")
		final Set<T> allAppendixesOfTypeT = (Set<T>) this.getAllAppendixesOfClass(appendixClass)
				.collect(Collectors.toSet());

		return allAppendixesOfTypeT;
	}

	private Stream<ApplicationAppendix<?>> getAllAppendixesOfClass(
			final Class<? extends ApplicationAppendix<?>> appendixClass) {
		final Stream<ApplicationAppendix<?>> stream = this.appendixes.stream()
				.filter(a -> a.getClass() == appendixClass);
		return stream;
	}

	public void addAppendix(final ApplicationAppendix<?> additionalAppendix) {
		this.appendixes.add(additionalAppendix);
	}

	public void addAppendix(final Collection<ApplicationAppendix<?>> additionalAppendixes) {
		this.appendixes.addAll(additionalAppendixes);
	}

	public List<ApplicationAppendix<?>> getAllAppendixesOfOwnerAsList(final ApplicationOwner owner) {
		return this.appendixes.stream()
				.filter(a -> a.getOwner().equals(owner.getOwnerId()))
				.collect(Collectors.toList());
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder();
		sb.append(this.getClass().getName())
				.append(": number of appendixes: ")
				.append(this.appendixes.size())
				.append('\n');
		this.appendixes.stream()
				.forEach(aa -> aa.toString(sb, 1));

		return sb.toString();
	}
}