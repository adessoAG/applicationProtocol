package de.adesso.example.framework;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class AppendixList {
	/**
	 * List of all appendix values.
	 */
	public List<ApplicationAppendix> appendixes;

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
	@SuppressWarnings("unchecked")
	public <T> T getAppendixOfType(UUID appendixId) throws TooManyElementsException {
		List<ApplicationAppendix> allAppendixesOfTypeT = getAllAppendixesOfTypeT(appendixId);
		if (allAppendixesOfTypeT.size() > 1) {
			throw new TooManyElementsException("more than one element");
		}

		return (T) allAppendixesOfTypeT.get(0);
	}
	
	/**
	 * Retrieves all appendixes of given type. The list may be empty if there is 
	 * no such element in the list. 
	 * 
	 * @param <T>
	 * @param appendixId
	 * @return
	 */
	public <T> List<T> getAllAppenixesOfType(UUID appendixId){
		@SuppressWarnings("unchecked")
		List<T> allAppendixesOfTypeT = (List<T>)getAllAppendixesOfTypeT(appendixId);
		
		return allAppendixesOfTypeT;
	}

	private List<ApplicationAppendix> getAllAppendixesOfTypeT(UUID appendixId) {
		List<ApplicationAppendix> allAppendixesOfTypeT = this.appendixes
				.stream()
				.filter(a -> a.getApplicationAppendixId() == appendixId)
				.collect(Collectors.toList());
		return allAppendixesOfTypeT;
	}
}