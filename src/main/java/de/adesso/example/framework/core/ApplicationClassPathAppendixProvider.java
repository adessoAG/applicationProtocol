package de.adesso.example.framework.core;

import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.AnnotationMetadata;

import de.adesso.example.framework.annotation.Appendix;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class ApplicationClassPathAppendixProvider extends ClassPathScanningCandidateComponentProvider {

	public ApplicationClassPathAppendixProvider() {
		super(false);
	}

	@Override
	protected boolean isCandidateComponent(final AnnotatedBeanDefinition beanDefinition) {
		final AnnotationMetadata metadata = beanDefinition.getMetadata();
		final String appendixClassName = Appendix.class.getName();
		final boolean isCandidate = metadata.isConcrete() && metadata.hasAnnotation(appendixClassName);

		log.atInfo().log("the class %s is candidate for appendix: %b", appendixClassName, isCandidate);
		return isCandidate;
	}
}
