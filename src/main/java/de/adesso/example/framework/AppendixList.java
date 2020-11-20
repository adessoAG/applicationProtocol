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

	AppendixList() {
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
	public <T> Optional<ApplicationAppendix<T>> getAppendixOfTypeT(
			final Class<? extends ApplicationAppendix<T>> appendixClass) throws TooManyElementsException {
		@SuppressWarnings("unchecked")
		final List<ApplicationAppendix<T>> allAppendixesOfTypeT = this.getAllAppendixesOfClass(appendixClass)
				.map(a -> (ApplicationAppendix<T>) a)
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
		final List<ApplicationAppendix<?>> allAppendixesOfTypeT = this.getAllAppendixesOfClass(appendixClass)
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
	public List<ApplicationAppendix<?>> getAllAppenixesAsList() {
		return this.appendixes;
	}

	/**
	 * Retrieves all appendixes of given type. The list may be empty if there is no
	 * such element in the list.
	 *
	 * @param <T>
	 * @param appendixId
	 * @return
	 */
	public List<ApplicationAppendix<?>> getAllAppenixesOfTypeAsList(
			final Class<? extends ApplicationAppendix<?>> appendixClass) {
		final List<ApplicationAppendix<?>> allAppendixesOfTypeT = this.getAllAppendixesOfClass(appendixClass)
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
	public <T> List<ApplicationAppendix<T>> getAllAppenixesOfTypeAsListT(
			final Class<? extends ApplicationAppendix<T>> appendixClass) {
		@SuppressWarnings("unchecked")
		final List<ApplicationAppendix<T>> allAppendixesOfTypeT = this.getAllAppendixesOfClass(appendixClass)
				.map(a -> (ApplicationAppendix<T>) a)
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
	public Set<ApplicationAppendix<?>> getAllAppenixesOfTypeAsSet(
			final Class<? extends ApplicationAppendix<?>> appendixClass) {
		final Set<ApplicationAppendix<?>> allAppendixesOfTypeT = this.getAllAppendixesOfClass(appendixClass)
				.collect(Collectors.toSet());

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
	public <T> Set<ApplicationAppendix<T>> getAllAppenixesOfTypeAsSetT(
			final Class<? extends ApplicationAppendix<T>> appendixClass) {
		@SuppressWarnings("unchecked")
		final Set<ApplicationAppendix<T>> allAppendixesOfTypeT = this.getAllAppendixesOfClass(appendixClass)
				.map(a -> (ApplicationAppendix<T>) a)
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

	public <T> void removeAll(final Class<? extends ApplicationAppendix<T>> appendixType) {
		@SuppressWarnings("unchecked")
		final List<ApplicationAppendix<T>> existingAppendixes = this.getAllAppendixesOfClass(appendixType)
				.map(a -> (ApplicationAppendix<T>) a)
				.collect(Collectors.toList());
		this.appendixes.removeAll(existingAppendixes);
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