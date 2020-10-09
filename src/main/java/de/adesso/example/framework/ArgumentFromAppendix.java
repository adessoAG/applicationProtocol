package de.adesso.example.framework;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

import lombok.Getter;

@Getter
public class ArgumentFromAppendix extends Argument {
	private final UUID requiredAttachment;
	
	public ArgumentFromAppendix(Class<?> type, UUID requiredAttachment) {
		super(type);
		this.requiredAttachment = requiredAttachment;
	}

	@Override
	protected Object prepareArgument(ApplicationProtocol<?> state, Object[] args) {
		Object result = null;
		if (Collection.class.isAssignableFrom(getType())){
			if (List.class.isAssignableFrom(getType())) {
				result = state.getAllAppenixesOfTypeAsList(requiredAttachment);
				
			} else {
				throw new NotYetImplementedException("cannot handle this");
			}
			
		} else {
			result = state.getAppendixOfType(requiredAttachment);
		}
		
		return result;
	}
}
