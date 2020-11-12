package de.adesso.example.framework;

import de.adesso.example.framework.annotation.Emulated;
import de.adesso.example.framework.annotation.Implementation;

@Emulated
public interface AnnotatedEmulatedInterfaceOtherEmulatedInterface {

	String method_1 = "operation";

	@Implementation(implementations = { OtherAnnotatedEmulatedInterface.class })
	ApplicationProtocol<String> operation(String aString, ApplicationProtocol<String> state);
}