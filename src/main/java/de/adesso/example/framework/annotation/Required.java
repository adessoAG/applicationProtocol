package de.adesso.example.framework.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Beans provided for the emulation of an interface may be called only if
 * specific parameters are given. Annotating an parameter as required tells the
 * system, that the method can only be called, if the parameter is available.
 *
 * @author Matthias
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
@Documented
public @interface Required {

	/**
	 * If the required parameter is a collection it might be specified, that the
	 * collection should have at least one element.
	 *
	 * @return if at least one element is required.
	 */
	boolean requireNotEmpty() default false;
}
