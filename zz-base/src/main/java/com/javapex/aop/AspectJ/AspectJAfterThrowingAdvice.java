package com.javapex.aop.AspectJ;

import java.lang.reflect.Method;

import com.javapex.aop.config.AspectInstanceFactory;
import org.aopalliance.intercept.MethodInvocation;
public class AspectJAfterThrowingAdvice extends AbstractAspectJAdvice  {


    public AspectJAfterThrowingAdvice(Method adviceMethod, AspectJExpressionPointcut pointcut, AspectInstanceFactory adviceObjectFactory) {
        super(adviceMethod, pointcut, adviceObjectFactory);
    }
    public Object invoke(MethodInvocation mi) throws Throwable {
        try {
            return mi.proceed();
        }
        catch (Throwable t) {
            invokeAdviceMethod();
            throw t;
        }
    }
}
