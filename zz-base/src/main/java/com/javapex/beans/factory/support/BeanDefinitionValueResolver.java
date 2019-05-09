package com.javapex.beans.factory.support;

import com.javapex.beans.BeanDefinition;
import com.javapex.beans.BeansException;
import com.javapex.beans.factory.BeanCreationException;
import com.javapex.beans.factory.FactoryBean;
import com.javapex.beans.factory.config.RuntimeBeanReference;
import com.javapex.beans.factory.config.TypedStringValue;

public class BeanDefinitionValueResolver {
    private final AbstractBeanFactory  beanFactory;

    public BeanDefinitionValueResolver(
            AbstractBeanFactory beanFactory) {

        this.beanFactory = beanFactory;
    }

    public Object resolveValueIfNecessary(Object value) {

        if (value instanceof RuntimeBeanReference) {
            RuntimeBeanReference ref = (RuntimeBeanReference) value;
            String refName = ref.getBeanName();
            Object bean = this.beanFactory.getBean(refName);
            return bean;

        }else if (value instanceof TypedStringValue) {
            return ((TypedStringValue) value).getValue();
        } else if (value instanceof BeanDefinition) {
            // Resolve plain BeanDefinition, without contained name: use dummy name.
            BeanDefinition bd = (BeanDefinition) value;

            String innerBeanName = "(inner bean)" + bd.getBeansClassName() + "#" +
                    Integer.toHexString(System.identityHashCode(bd));

            return resolveInnerBean(innerBeanName, bd);

        }
        else{
            return value;
        }
    }
    private Object resolveInnerBean(String innerBeanName, BeanDefinition innerBd) {

        try {

            Object innerBean = this.beanFactory.createBean(innerBd);

            if (innerBean instanceof FactoryBean) {
                try {
                    return ((FactoryBean<?>)innerBean).getObject();
                } catch (Exception e) {
                    throw new BeanCreationException(innerBeanName, "FactoryBean threw exception on object creation", e);
                }
            }
            else {
                return innerBean;
            }
        }
        catch (BeansException ex) {
            throw new BeanCreationException(
                    innerBeanName,
                    "Cannot create inner bean '" + innerBeanName + "' " +
                            (innerBd != null && innerBd.getBeansClassName() != null ? "of type [" + innerBd.getBeansClassName() + "] " : "")
                    , ex);
        }
    }
}
