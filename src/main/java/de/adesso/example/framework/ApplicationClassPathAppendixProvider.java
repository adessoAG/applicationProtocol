package de.adesso.example.framework;

import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.AnnotationMetadata;

import de.adesso.example.framework.annotation.Appendix;

public class ApplicationClassPathAppendixProvider extends ClassPathScanningCandidateComponentProvider {

	public ApplicationClassPathAppendixProvider() {
		super(false);
	}

	@Override
	protected boolean isCandidateComponent(final AnnotatedBeanDefinition beanDefinition) {
		final AnnotationMetadata metadata = beanDefinition.getMetadata();
		return (metadata.isConcrete() && metadata.hasAnnotation(Appendix.class.getName())
				&& ApplicationAppendix.class.isAssignableFrom(metadata.getClass()));
	}
}
