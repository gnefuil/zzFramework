package com.javapex.beans;

import com.javapex.beans.factory.support.ConstructorArgument;

import java.util.List;

public interface BeanDefinition {

    public static final String SCOPE_SINGLETON = "singleton";
    public static final String SCOPE_PROTOTYPE = "prototype";
    public static final String SCOPE_DEFAULT = "";

    boolean isSingleton();
    boolean isPrototype();
    String getScope();
    void setScope(String scope);

    String getBeansClassName();

    List<PropertyValue> getPropertyValues();
    ConstructorArgument getConstructorArgument();
    String getID();
    boolean hasConstructorArgumentValues();

    Class<?> resolveBeanClass(ClassLoader classLoader) throws ClassNotFoundException;
    Class<?> getBeanClass() throws IllegalStateException ;
    boolean hasBeanClass();

    boolean isSynthetic();
}
