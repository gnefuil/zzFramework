package com.javapex.aop.AspectJ;
import java.lang.reflect.Method;

import com.javapex.aop.config.AspectInstanceFactory;
import org.aopalliance.intercept.MethodInvocation;
public class AspectJAfterReturningAdvice extends AbstractAspectJAdvice{

    public AspectJAfterReturningAdvice(Method adviceMethod, AspectJExpressionPointcut pointcut, AspectInstanceFactory adviceObjectFactory){
        super(adviceMethod,pointcut,adviceObjectFactory);
    }

    public Object invoke(MethodInvocation mi) throws Throwable {
        Object o = mi.proceed();
        //例如：调用TransactionManager的commit方法
        this.invokeAdviceMethod();
        return o;
    }
}
