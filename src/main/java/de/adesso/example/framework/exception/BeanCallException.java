package de.adesso.example.framework.exception;

import java.lang.reflect.Method;

import lombok.extern.log4j.Log4j2;

@Log4j2
public class BeanCallException extends RuntimeException {

	private static final long serialVersionUID = -8390406591314015878L;

	private BeanCallException(final String message, final Throwable e) {
		super(message, e);
	}

	private BeanCallException(final String message) {
		super(message);
	}

	public static BeanCallException callFailedWithException(final Class<?> beanClass, final Method method,
			final Throwable th) {
		final String message = String.format("bean invocation through exception: %s::%s -> %s",
				beanClass.getClass().getName(),
				method.getName(),
				th.getClass().getName());
		log.atError().log(message);
		log.atError().log(th);

		return new BeanCallException(message, th);
	}

	public static BeanCallException callFailedMissingParameter(final Class<?> beanClass, final Method method,
			final String parameterName) {
		final String message = String.format("bean invocation required parameters, but missing: %s::%s -> %s",
				beanClass.getClass().getName(),
				method.getName(),
				parameterName);
		log.atError().log(message);

		return new BeanCallException(message);
	}

	public static BeanCallException callFailedOnStrategy(final Class<?> beanClass, final Method method) {
		final String message = String.format("bean invocation failed due to strategy: %s::%s",
				beanClass.getClass().getName(),
				method.getName());
		log.atError().log(message);

		return new BeanCallException(message);
	}
}
