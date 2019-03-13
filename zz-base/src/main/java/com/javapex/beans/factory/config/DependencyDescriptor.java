package com.javapex.beans.factory.config;

import com.javapex.util.Assert;

import java.lang.reflect.Field;

public class DependencyDescriptor {
    private  Field field;
    private boolean required;

    public DependencyDescriptor(Field field, boolean required) {
        Assert.notNull(field, "Field must not be null");
        this.field = field;
        this.required = required;

    }
    public Class<?> getDependencyType(){
        if(this.field != null){
            return field.getType();
        }
        throw new RuntimeException("only support field dependency");
    }

    public boolean isRequired() {
        return this.required;
    }
}
