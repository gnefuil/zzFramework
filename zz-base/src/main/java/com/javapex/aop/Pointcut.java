package com.javapex.aop;

public interface Pointcut {
    MethodMatcher getMethodMatcher();
    String getExpression();
}
