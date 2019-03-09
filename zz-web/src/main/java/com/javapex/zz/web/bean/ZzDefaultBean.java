package com.javapex.zz.web.bean;

public class ZzDefaultBean implements ZzBeanFactory{
    @Override
    public void register(Object object) {

    }

    @Override
    public Object getBean(String name) throws Exception {
        Class<?> aClass = Class.forName(name);
        return aClass.newInstance();
    }

    @Override
    public void releaseBean() {
    }
}
