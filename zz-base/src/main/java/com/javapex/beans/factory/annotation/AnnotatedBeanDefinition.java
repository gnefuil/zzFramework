package com.javapex.beans.factory.annotation;

import com.javapex.beans.BeanDefinition;
import com.javapex.core.type.AnnotationMetadata;

public interface AnnotatedBeanDefinition extends BeanDefinition {
    AnnotationMetadata getMetadata();
}
