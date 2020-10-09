package de.adesso.example.framework;

import java.util.List;
import java.util.UUID;

import lombok.Getter;
import lombok.Setter;

/**
 * Basic protocol element for the application protocol. Users should extend this
 * class to add their own attributes.
 * 
 * @author Matthias
 *
 */

public class ApplicationProtocol <RESULT_TYPE>{
	private AppendixList data = new AppendixList();

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
	public <T> T getAppendixOfType(UUID appendixId) throws TooManyElementsException {
		return data.getAppendixOfType(appendixId);
	}
	
	/**
	 * Retrieves all appendixes of given type. The list may be empty if there is 
	 * no such element in the list. 
	 * 
	 * @param <T>
	 * @param appendixId
	 * @return
	 */
	public <T> List<T> getAllAppenixesOfTypeAsList(UUID appendixId){
		return data.getAllAppenixesOfType(appendixId);
	}
}
