package de.adesso.example.framework.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Provide a calling strategy for a call of a bean.
 *
 * @author Matthias
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Documented
public @interface CallStrategy {

	/**
	 * Provide the strategy
	 */
	CallingStrategy strategy() default CallingStrategy.Eager;

}
