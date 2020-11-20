package de.adesso.example.framework;

import de.adesso.example.framework.annotation.Emulated;
import de.adesso.example.framework.annotation.Implementation;
import de.adesso.example.framework.annotation.ImplementationDefinition;

@Emulated
public interface AnnotatedEmulatedInterfaceSpringBeanInterface {

	String method_1 = "operation";

	@ImplementationDefinition(value = @Implementation(bean = TestSpringBean.class))
	ApplicationProtocol<String> operation(String aString, ApplicationProtocol<String> state);
}