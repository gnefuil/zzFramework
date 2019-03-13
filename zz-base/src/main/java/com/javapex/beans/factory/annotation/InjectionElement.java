package com.javapex.beans.factory.annotation;

import com.javapex.beans.factory.config.AutowireCapableBeanFactory;

import java.lang.reflect.Member;

public abstract class InjectionElement {

    protected Member member;
    protected AutowireCapableBeanFactory factory;

    InjectionElement(Member member,AutowireCapableBeanFactory factory){
        this.member = member;
        this.factory = factory;
    }

    public abstract void inject(Object target);
}
