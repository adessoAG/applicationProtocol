package de.adesso.example.framework;

import de.adesso.example.framework.annotation.Emulated;
import de.adesso.example.framework.annotation.Implementation;

@Emulated
public interface AnnotatedEmulatedInterfaceSpringBeanInterface {

	String method_1 = "operation";

	@Implementation(implementations = { TestSpringBean.class })
	ApplicationProtocol<String> operation(String aString, ApplicationProtocol<String> state);
}