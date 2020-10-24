package de.adesso.example.framework.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This annotation is the advice for the application framework to ensure, that
 * every implementation annotated at the interface method has to provide this
 * parameter. This is a proper way to define on which information the
 * calculation has to be based on.
 *
 * @author Matthias
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
@Documented
public @interface RequiredParameter {
}
