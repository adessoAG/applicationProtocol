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
		final Object parameter = args[this.sourcePosition];
		this.validateArgument(parameter);
		return parameter;
	}

	private void validateSourcePosition() {
		if (this.sourcePosition > this.getBeanOperation().getMethodImplementation().getMethod()
				.getParameters().length) {
			throw BuilderException.sourcePositionOutOfRange(this.getBean(), this.getBeanMethodIdentifier(),
					this.getBeanOperation().getMethodImplementation().getDispatcher().getImplementationInterface());
		}

		if (!this.getSourceParameter().getClass().isAssignableFrom(this.getTargetParameter())) {
			throw BuilderException.parameterNotAssignable(
					this.getEmulatedInterface(),
					this.getEmulatedInterfaceMethod(),
					this.getBean(),
					this.getBeanOperation().getMethodIdentifier(),
					this.getParameterName());
		}
	}

	private Parameter getSourceParameter() {
		return this.getBeanOperation().getMethodImplementation().getMethod().getParameters()[this.sourcePosition];
	}
}
