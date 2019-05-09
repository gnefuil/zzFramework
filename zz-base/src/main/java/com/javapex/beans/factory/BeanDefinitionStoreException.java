package com.javapex.beans.factory;

import com.javapex.beans.BeansException;

public class BeanDefinitionStoreException extends BeansException {
    public BeanDefinitionStoreException(String msg) {
        super(msg);
    }
    public BeanDefinitionStoreException(String message, Throwable cause) {
        super(message, cause);
    }
}
