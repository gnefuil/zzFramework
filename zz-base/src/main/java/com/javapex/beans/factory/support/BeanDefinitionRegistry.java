package com.javapex.beans.factory.support;

import com.javapex.beans.BeanDefinition;

public interface BeanDefinitionRegistry {
    BeanDefinition getBeanDefinition(String beanID);
    void registerBeanDefinition(String beanID, BeanDefinition bd);

}
