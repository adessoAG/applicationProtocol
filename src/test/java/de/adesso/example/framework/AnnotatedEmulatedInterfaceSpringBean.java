package de.adesso.example.framework;

import de.adesso.example.framework.annotation.Emulated;
import de.adesso.example.framework.annotation.Implementation;

@Emulated
public interface AnnotatedEmulatedInterfaceSpringBean {

	String method_1 = "operation";

	@Implementation(implementations = { TestSpringBeanImpl.class })
	ApplicationProtocol<String> operation(String aString, ApplicationProtocol<String> state);
}