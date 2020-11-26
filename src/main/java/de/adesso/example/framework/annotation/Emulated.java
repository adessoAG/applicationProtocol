package de.adesso.example.framework.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import de.adesso.example.framework.core.DaisyChainDispatcher;

/**
 * All interfaces annotated as emulated will be implemented with help of the
 * {@link DaisyChainDispatcher}.
 *
 * @author Matthias Brenner
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
public @interface Emulated {

}
