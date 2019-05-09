package com.javapex.test.v5;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import com.javapex.aop.AspectJ.AspectJAfterReturningAdvice;
import com.javapex.aop.AspectJ.AspectJAfterThrowingAdvice;
import com.javapex.aop.AspectJ.AspectJBeforeAdvice;
import com.javapex.aop.AspectJ.AspectJExpressionPointcut;
import com.javapex.aop.config.AspectInstanceFactory;
import com.javapex.aop.framework.ReflectiveMethodInvocation;
import com.javapex.beans.factory.BeanFactory;
import com.javapex.service.v5.PetStoreService;
import com.javapex.tx.TransactionManager;
import com.javapex.util.MessageTracker;
import org.aopalliance.intercept.MethodInterceptor;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
public class ReflectiveMethodInvocationTest extends AbstractV5Test{

    private AspectJBeforeAdvice beforeAdvice = null;
    private AspectJAfterReturningAdvice afterAdvice = null;
    private AspectJAfterThrowingAdvice afterThrowingAdvice = null;
    private AspectJExpressionPointcut pc = null;
    private BeanFactory beanFactory = null;
    private AspectInstanceFactory aspectInstanceFactory = null;
    private PetStoreService petStoreService = null;
    private TransactionManager tx;

    @Before
    public  void setUp() throws Exception{
        petStoreService = new PetStoreService();
        tx = new TransactionManager();

        beanFactory = this.getBeanFactory("petstore-v5.xml");
        aspectInstanceFactory = this.getAspectInstanceFactory("tx");
        aspectInstanceFactory.setBeanFactory(beanFactory);

        MessageTracker.clearMsgs();
        beforeAdvice = new AspectJBeforeAdvice(
                this.getAdviceMethod("start"),
                null,
                aspectInstanceFactory);

        afterAdvice = new AspectJAfterReturningAdvice(
                this.getAdviceMethod("commit"),
                null,
                aspectInstanceFactory);

        afterThrowingAdvice = new AspectJAfterThrowingAdvice(
                this.getAdviceMethod("rollback"),
                null,
                aspectInstanceFactory
        );

    }

    @Test
    public void testMethodInvocation() throws Throwable{


        Method targetMethod = PetStoreService.class.getMethod("placeOrder");

        List<MethodInterceptor> interceptors = new ArrayList<MethodInterceptor>();
        interceptors.add(beforeAdvice);
        interceptors.add(afterAdvice);


        ReflectiveMethodInvocation mi = new ReflectiveMethodInvocation(petStoreService,targetMethod,new Object[0],interceptors);

        mi.proceed();


        List<String> msgs = MessageTracker.getMsgs();
        Assert.assertEquals(3, msgs.size());
        Assert.assertEquals("start tx", msgs.get(0));
        Assert.assertEquals("place order", msgs.get(1));
        Assert.assertEquals("commit tx", msgs.get(2));

    }

    @Test
    public void testMethodInvocation2() throws Throwable{


        Method targetMethod = PetStoreService.class.getMethod("placeOrder");

        List<MethodInterceptor> interceptors = new ArrayList<MethodInterceptor>();
        interceptors.add(afterAdvice);
        interceptors.add(beforeAdvice);



        ReflectiveMethodInvocation mi = new ReflectiveMethodInvocation(petStoreService,targetMethod,new Object[0],interceptors);

        mi.proceed();


        List<String> msgs = MessageTracker.getMsgs();
        Assert.assertEquals(3, msgs.size());
        Assert.assertEquals("start tx", msgs.get(0));
        Assert.assertEquals("place order", msgs.get(1));
        Assert.assertEquals("commit tx", msgs.get(2));

    }
    @Test
    public void testAfterThrowing() throws Throwable{


        Method targetMethod = PetStoreService.class.getMethod("placeOrderWithException");

        List<MethodInterceptor> interceptors = new ArrayList<MethodInterceptor>();
        interceptors.add(afterThrowingAdvice);
        interceptors.add(beforeAdvice);



        ReflectiveMethodInvocation mi = new ReflectiveMethodInvocation(petStoreService,targetMethod,new Object[0],interceptors);
        try{
            mi.proceed();

        }catch(Throwable t){
            List<String> msgs = MessageTracker.getMsgs();
            Assert.assertEquals(2, msgs.size());
            Assert.assertEquals("start tx", msgs.get(0));
            Assert.assertEquals("rollback tx", msgs.get(1));
            return;
        }


        Assert.fail("No Exception thrown");


    }
}
