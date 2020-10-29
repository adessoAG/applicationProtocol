package de.adesso.example.framework.core;

import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.AnnotationMetadata;

import de.adesso.example.framework.annotation.Emulated;

public class ApplicationClassPathEmulationInterfaceProvider extends ClassPathScanningCandidateComponentProvider {

	public ApplicationClassPathEmulationInterfaceProvider() {
		super(false);
	}

	@Override
	protected boolean isCandidateComponent(final AnnotatedBeanDefinition beanDefinition) {
		final AnnotationMetadata metadata = beanDefinition.getMetadata();
		return (metadata.isInterface() && metadata.hasAnnotation(Emulated.class.getName()));
	}
}
