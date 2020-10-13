package de.adesso.example.framework;

public interface EmulatedInterface {

	String method_1 = "operation";
	String method_2 = "otherOperation";

	ApplicationProtocol<String> operation(String aString, int anInt, Integer anInteger, String anotherString);

	ApplicationProtocol<String> otherOperation(String aString);
}