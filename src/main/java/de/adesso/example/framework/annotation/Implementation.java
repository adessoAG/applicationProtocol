package de.adesso.example.framework.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Documented
public @interface Implementation {

	Class<?>[] implementations();

	MatchingStrategy[] strategy() default { MatchingStrategy.ByType, MatchingStrategy.FromAppendix,
			MatchingStrategy.ByName };
}
