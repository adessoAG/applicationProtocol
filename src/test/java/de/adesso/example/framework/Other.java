package de.adesso.example.framework;

import lombok.Getter;

@Getter
public class Other {

	private final String aString;
	private final Integer aInteger;

	public Other(final String aString, final Integer aInteger) {
		this.aString = aString;
		this.aInteger = aInteger;
	}
}
