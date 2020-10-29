package de.adesso.example.framework.core;

import java.lang.reflect.Parameter;

import de.adesso.example.framework.ApplicationProtocol;
import de.adesso.example.framework.exception.BuilderException;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NonNull;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class ArgumentFromMethod extends Argument {

	/**
	 * position of the argument on the initial call. Counts from 0.
	 */
	@Getter(value = AccessLevel.PACKAGE)
	private final int sourcePosition;

	public ArgumentFromMethod(final @NonNull Class<?> type, final int sourcePosition) {
		super(type);
		if (sourcePosition < 0) {
			final String message = "position may not be negative";
			log.atError().log(message);
			throw new IndexOutOfBoundsException(message);
		}
		// at this moment the Method and the parameter declarations are not available.
		// Position will be validated during initialization.
		this.sourcePosition = sourcePosition;
	}

	@Override
	void init(final BeanOperation beanOperation, final Parameter parameter, final int targetPosition) {
		super.init(beanOperation, parameter, targetPosition);
		this.validateSourcePosition();
	}

	@Override
	protected Object prepareArgument(final ApplicationProtocol<?> state, final Object[] args) {
		return args[this.sourcePosition];
	}

	private void validateSourcePosition() {
		if (this.sourcePosition > this.getBeanOperation().getMethodImplementation().getMethod()
				.getParameters().length) {
			final String message = String.format(
					"parameter position is greater as the method parameters (%s::%s) available from the describing interface(%s)",
					this.getBeanOperation().getClazz().getName(),
					this.getBeanMethodIdentifier(),
					this.getBeanOperation().getMethodImplementation().getDispatcher().getImplementationInterface()
							.getName());
			log.atError().log(message);
			throw new BuilderException(message);
		}

		if (!this.getSourceParameter().getClass().isAssignableFrom(this.getTargetParameter())) {
			final String message = String.format(
					"parameter of interface %s::%s cannot be assigned to bean %s::%s.",
					this.getEmulatedInterface().getName(),
					this.getEmulatedInterfaceMethod().getName(),
					this.getBean(),
					this.getEmulatedInterface().getName());
			log.atError().log(message);
			throw new BuilderException(message);
		}
	}

	private Parameter getSourceParameter() {
		return this.getBeanOperation().getMethodImplementation().getMethod().getParameters()[this.sourcePosition];
	}
}
