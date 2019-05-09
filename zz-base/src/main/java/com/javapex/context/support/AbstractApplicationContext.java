package com.javapex.context.support;

import com.javapex.aop.AspectJ.AspectJAutoProxyCreator;
import com.javapex.beans.factory.NoSuchBeanDefinitionException;
import com.javapex.beans.factory.annotation.AutowiredAnnotationProcessor;
import com.javapex.beans.factory.config.ConfigurableBeanFactory;
import com.javapex.beans.factory.support.DefaultBeanFactory;
import com.javapex.beans.factory.xml.XmlBeanDefinitionReader;
import com.javapex.context.ApplicationContext;
import com.javapex.core.io.Resource;
import com.javapex.util.ClassUtils;

import java.util.List;

public abstract class AbstractApplicationContext implements ApplicationContext {
    private ClassLoader beanClassLoader;
    private DefaultBeanFactory defaultBeanFactory = null;

    public AbstractApplicationContext(String configFile) {
        defaultBeanFactory = new DefaultBeanFactory();
        XmlBeanDefinitionReader xmlBeanDefinitionReader = new XmlBeanDefinitionReader(defaultBeanFactory);
        Resource resource = this.getResourceByPath(configFile);
        xmlBeanDefinitionReader.loadBeanDefinitions(resource);
        defaultBeanFactory.setBeanClassLoader(this.getBeanClassLoader());//TODO 有什么问题？
        registerBeanPostProcessors(defaultBeanFactory);
    }

    public Object getBean(String beanID) {
        return defaultBeanFactory.getBean(beanID);
    }

    protected abstract Resource getResourceByPath(String path);

    public void setBeanClassLoader(ClassLoader beanClassLoader) {
        this.beanClassLoader = beanClassLoader;
    }

    public ClassLoader getBeanClassLoader() {
        return (this.beanClassLoader != null ? this.beanClassLoader : ClassUtils.getDefaultClassLoader());
    }

    protected void registerBeanPostProcessors(ConfigurableBeanFactory beanFactory) {

        {
            AutowiredAnnotationProcessor postProcessor = new AutowiredAnnotationProcessor();
            postProcessor.setBeanFactory(beanFactory);
            beanFactory.addBeanPostProcessor(postProcessor);
        }
        {
            AspectJAutoProxyCreator postProcessor = new AspectJAutoProxyCreator();
            postProcessor.setBeanFactory(beanFactory);
            beanFactory.addBeanPostProcessor(postProcessor);
        }
    }


    public Class<?> getType(String name) throws NoSuchBeanDefinitionException {
        return this.defaultBeanFactory.getType(name);
    }

    public List<Object> getBeansByType(Class<?> type){
        return this.defaultBeanFactory.getBeansByType(type);
    }
}
