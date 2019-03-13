package com.javapex.context.annotation;

import com.javapex.beans.BeanDefinition;
import com.javapex.beans.factory.annotation.AnnotatedBeanDefinition;
import com.javapex.beans.factory.support.BeanDefinitionRegistry;
import com.javapex.beans.factory.support.BeanNameGenerator;
import com.javapex.core.annotation.AnnotationAttributes;
import com.javapex.core.type.AnnotationMetadata;
import com.javapex.stereotype.Component;
import com.javapex.util.ClassUtils;
import com.javapex.util.StringUtils;

import java.beans.Introspector;
import java.util.Map;
import java.util.Set;

public class AnnotationBeanNameGenerator implements BeanNameGenerator {

    public String generateBeanName(BeanDefinition definition, BeanDefinitionRegistry registry) {
        if (definition instanceof AnnotatedBeanDefinition) {
            String beanName = determineBeanNameFromAnnotation((AnnotatedBeanDefinition) definition);
            if (StringUtils.hasText(beanName)) {
                // Explicit bean name found.
                return beanName;
            }
        }
        return buildDefaultBeanName(definition, registry);
    }

    /**
     * Derive a bean name from one of the annotations on the class.
     * @param annotatedDef the annotation-aware bean definition
     * @return the bean name, or {@code null} if none is found
     */
    protected String determineBeanNameFromAnnotation(AnnotatedBeanDefinition annotatedDef) {
        AnnotationMetadata amd = annotatedDef.getMetadata();
        Set<String> types = amd.getAnnotationTypes();
        String beanName = null;
        for (String type : types) {
            AnnotationAttributes attributes = amd.getAnnotationAttributes(type);
            if (isStereotypeWithNameValue(type,attributes)) {
                Object value = attributes.get("value");
                if (value instanceof String) {
                    String strVal = (String) value;
                    if (StringUtils.hasLength(strVal)) {
                        beanName = strVal;
                    }
                }
            }
        }
        return beanName;
    }

    protected boolean isStereotypeWithNameValue(String annotationType,Map<String, Object> attributes) {

        boolean isStereotype = annotationType.equals(Component.class.getName()); /*||
				(metaAnnotationTypes != null && metaAnnotationTypes.contains(COMPONENT_ANNOTATION_CLASSNAME)) ||
				annotationType.equals("javax.annotation.ManagedBean") ||
				annotationType.equals("javax.inject.Named");*/

        return (isStereotype && attributes != null && attributes.containsKey("value"));
    }
    /**
     * Derive a default bean name from the given bean definition.
     * <p>The default implementation delegates to {@link #buildDefaultBeanName(BeanDefinition)}.
     * @param definition the bean definition to build a bean name for
     * @param registry the registry that the given bean definition is being registered with
     * @return the default bean name (never {@code null})
     */
    protected String buildDefaultBeanName(BeanDefinition definition, BeanDefinitionRegistry registry) {
        return buildDefaultBeanName(definition);
    }

    /**
     * Derive a default bean name from the given bean definition.
     * <p>The default implementation simply builds a decapitalized version
     * of the short class name: e.g. "mypackage.MyJdbcDao" -> "myJdbcDao".
     * <p>Note that inner classes will thus have names of the form
     * "outerClassName.InnerClassName", which because of the period in the
     * name may be an issue if you are autowiring by name.
     * @param definition the bean definition to build a bean name for
     * @return the default bean name (never {@code null})
     */
    protected String buildDefaultBeanName(BeanDefinition definition) {
        String shortClassName = ClassUtils.getShortName(definition.getBeansClassName());
        return Introspector.decapitalize(shortClassName);
    }

}
