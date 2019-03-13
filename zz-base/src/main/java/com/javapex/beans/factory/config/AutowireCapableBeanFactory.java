package com.javapex.beans.factory.config;

import com.javapex.beans.factory.BeanFactory;

public interface AutowireCapableBeanFactory extends BeanFactory {
    Object resolveDependency(DependencyDescriptor descriptor);
}
