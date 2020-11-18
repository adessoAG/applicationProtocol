package de.adesso.example.framework.annotation;

import java.lang.annotation.Repeatable;

/**
 * An implementation for a emulated method is a method within a bean. The bean
 * is provided by its class. If the method is omitted, the default is, that the
 * method is queried within the specified class with the same identifier as the
 * method to be emulated.
 *
 * @author Matthias
 *
 */
@Repeatable(ImplementationDefinition.class)
public @interface Implementation {

	Class<?> bean();

	String method() default "";
}
