package de.adesso.example.framework;

import java.math.BigDecimal;
import java.util.ArrayList;

import de.adesso.example.framework.annotation.Emulated;
import de.adesso.example.framework.annotation.Implementation;
import de.adesso.example.framework.annotation.ImplementationDefinition;
import de.adesso.example.framework.annotation.RequiredParameter;

@Emulated
public interface AnnotatedEmulatedInterfacePojo {

	String method_1 = "operation";
	String method_2 = "requiredParameterOperation";
	String method_3 = "listOperation";
	String method_4 = "throwingOperation";

	@ImplementationDefinition(value = @Implementation(bean = TestPojoBean.class))
	ApplicationProtocol<String> operation(
			String aString,
			ApplicationProtocol<String> state);

	@ImplementationDefinition(value = @Implementation(bean = TestPojoBean.class))
	ApplicationProtocol<String> requiredParameterOperation(
			@RequiredParameter String aString,
			@RequiredParameter ApplicationProtocol<String> state);

	@ImplementationDefinition(value = @Implementation(bean = TestPojoBean.class))
	ApplicationProtocol<String> listOperation(
			@RequiredParameter ArrayList<String> aString,
			@RequiredParameter ApplicationProtocol<String> state);

	@ImplementationDefinition(value = @Implementation(bean = TestPojoBean.class))
	ApplicationProtocol<String> throwingOperation(
			@RequiredParameter String aString,
			@RequiredParameter ApplicationProtocol<String> state);

	@ImplementationDefinition(value = @Implementation(bean = TestPojoBean.class))
	ApplicationProtocol<String> operationOtherType(
			BigDecimal aBD,
			ApplicationProtocol<String> state);

	@ImplementationDefinition(value = @Implementation(bean = TestPojoBean.class))
	ApplicationProtocol<String> operationOtherType_2(
			BigDecimal aBD,
			ApplicationProtocol<String> state);
}