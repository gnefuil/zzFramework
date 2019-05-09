package com.javapex.beans.factory;

import com.javapex.aop.Advice;

import java.util.List;

public interface BeanFactory {
    Object getBean(String beanID);
    Class<?> getType(String name) throws NoSuchBeanDefinitionException;
    List<Object> getBeansByType(Class<?> type);
}
