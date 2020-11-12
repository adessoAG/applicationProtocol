package de.adesso.example.framework;

import de.adesso.example.framework.annotation.Emulated;
import de.adesso.example.framework.annotation.Implementation;

@Emulated
public interface AnnotatedEmulatedInterfacePojo {

	String method_1 = "operation";

	@Implementation(implementations = { TestPojoBean.class })
	ApplicationProtocol<String> operation(String aString, ApplicationProtocol<String> state);
}