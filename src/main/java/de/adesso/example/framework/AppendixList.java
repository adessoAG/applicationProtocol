/**
 * The MIT License (MIT)
 *
 * Copyright © 2020 Matthias Brenner and Adesso SE
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the “Software”), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED “AS IS”, WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *
 */
package de.adesso.example.framework;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import de.adesso.example.framework.exception.TooManyElementsException;

class AppendixList implements Serializable {

	private static final long serialVersionUID = 3547825846436619073L;
	/**
	 * List of all appendix values.
	 */
	private List<ApplicationAppendix<?>> appendixes = new ArrayList<>();

	/**
	 * Avoid instantiation from outside the package. This hinders that someone can
	 * influence the appendixes.
	 */
	AppendixList() {
	}

	/**
	 * Retrieves exactly one appendix of given type. If the list contains more than
	 * one appendix of this type, a {@link TooManyElementsException} is thrown. If
	 * the type T does not match to your local variable the runtime will throw a
	 * {@link ClassCastException}.
	 *
	 * @param <T>           the type of the requested appendix
	 * @param appendixClass the class of the requested appendix
	 * @return an optional of the requested type
	 * @throws TooManyElementsException if the list of appendixes contains more than
	 *                                  one element of type T
	 */
	<T> Optional<T> getAppendixOfTypeT(final Class<T> appendixClass) throws TooManyElementsException {
		final List<T> allAppendixesOfTypeT = this.getAllAppenixesOfTypeAsListT(appendixClass);

		if (allAppendixesOfTypeT.isEmpty()) {
			return Optional.empty();
		}
		if (allAppendixesOfTypeT.size() > 1) {
			throw new TooManyElementsException("more than one element");
		}

		return Optional.of(allAppendixesOfTypeT.get(0));
	}

	/**
	 * Retrieves all appendixes of given type. The list may be empty if there is no
	 * such element in the list.
	 *
	 * @param <T>           type of the appendix to retrieve
	 * @param appendixClass class of the appendix to retrieve
	 * @return a list of appendixes of type T
	 */
	<T> List<T> getAllAppenixesOfTypeAsListT(final Class<T> appendixClass) {
		final List<T> allAppendixesOfTypeT = this.getAllAppendixesOfClass(appendixClass)
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
	<T> Set<T> getAllAppenixesOfTypeAsSetT(final Class<T> appendixClass) {
		final Set<T> allAppendixesOfTypeT = this.getAllAppendixesOfClass(appendixClass)
				.collect(Collectors.toSet());

		return allAppendixesOfTypeT;
	}

	/**
	 * Add a new appendix. Creates the internal representation to be able to store
	 * owner with the corresponding appendix.
	 *
	 * @param <T>                type of the appendix
	 * @param owner              owner of the appendix
	 * @param additionalAppendix appendix to be added
	 */
	<T> void addAppendix(final ApplicationOwner owner, final T additionalAppendix) {
		this.appendixes.add(new ApplicationAppendix<>(owner, additionalAppendix));
	}

	/**
	 * Adds a collection of classes of probably different type to the list of
	 * appendixes.
	 *
	 * @param owner                the owner of the appendixes
	 * @param additionalAppendixes the collection of the appendixes to be added
	 */
	void addAppendix(final ApplicationOwner owner, final Collection<?> additionalAppendixes) {
		additionalAppendixes.stream()
				.forEach(t -> this.addAppendix(owner, t));
	}

	/**
	 * Removes all appendixes of given type of the owner from the list of the
	 * appendixes.
	 *
	 * @param owner        owner of the appendixes, has to match the owner provided
	 *                     at the time the appendix was provided
	 * @param appendixType class of the appendix
	 */
	<T> void removeAll(final ApplicationOwner owner, final Class<T> appendixType) {
		final List<ApplicationAppendix<?>> existingAppendixes = this.appendixes.stream()
				.filter(a -> a.content.getClass() == appendixType && a.owner == owner)
				.collect(Collectors.toList());
		this.appendixes.removeAll(existingAppendixes);
	}

	/**
	 * Retrieves all appendixes of given type. The list may be empty if there is no
	 * such element in the list. This method is not public, because it exposes all
	 * appendixes even if they are private to an owner.
	 * <p>
	 * This method is not intended for general use, since it exposes all different
	 * types of appendixes.
	 *
	 * @return a list of all appendixes of different types
	 */
	List<?> getAllAppenixesAsList() {
		return this.appendixes.stream()
				.map(a -> a.content)
				.collect(Collectors.toList());
	}

	/**
	 * Add all appendixes of the other list to the own list. Clear the other list.
	 * The appendixes are then only in this list.
	 *
	 * @param other the list which hands over their appendixes
	 */
	void transfer(final AppendixList other) {
		this.appendixes.addAll(other.appendixes);
		other.appendixes.clear();
	}

	AppendixList duplicate() {
		final AppendixList appendixList = new AppendixList();
		appendixList.appendixes = this.appendixes;

		return appendixList;
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

	private <T> Stream<T> getAllAppendixesOfClass(final Class<T> appendixClass) {
		@SuppressWarnings("unchecked")
		final Stream<T> stream = this.appendixes.stream()
				.filter(a -> appendixClass.isAssignableFrom(a.content.getClass()))
				.map(a -> (T) a.content);
		return stream;
	}

	private class ApplicationAppendix<T> {

		/** content of the appendix */
		final T content;
		/** owner id */
		final ApplicationOwner owner;

		ApplicationAppendix(final ApplicationOwner owner, final T content) {
			this.content = content;
			this.owner = owner;
		}

		public String toString() {
			final StringBuilder sb = new StringBuilder();
			return this.toString(sb, 0).toString();
		}

		public StringBuilder toString(final StringBuilder sb, final int indent) {
			this.identation(sb, indent)
					.append(this.getClass().getName())
					.append("(owner = ").append(this.owner).append("): ");
			sb.append(this.content.toString()).append('\n');
			return sb;
		}

		private StringBuilder identation(final StringBuilder sb, final int tabs) {
			for (int i = 0; i < tabs; i++) {
				sb.append('\t');
			}
			return sb;
		}
	}
}