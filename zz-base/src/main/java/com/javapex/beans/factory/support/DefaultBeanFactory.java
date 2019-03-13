package com.javapex.beans.factory.support;

import com.javapex.beans.BeanDefinition;
import com.javapex.beans.PropertyValue;
import com.javapex.beans.SimpleTypeConverter;
import com.javapex.beans.factory.BeanCreationException;
import com.javapex.beans.factory.config.BeanPostProcessor;
import com.javapex.beans.factory.config.ConfigurableBeanFactory;
import com.javapex.beans.factory.config.DependencyDescriptor;
import com.javapex.beans.factory.config.InstantiationAwareBeanPostProcessor;
import com.javapex.util.ClassUtils;
import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DefaultBeanFactory extends DefaultSingletonBeanRegistry
        implements BeanDefinitionRegistry,ConfigurableBeanFactory {

    private List<BeanPostProcessor> beanPostProcessors = new ArrayList<BeanPostProcessor>();

    private ClassLoader beanClassLoader;
    private final Map<String,BeanDefinition> beanDefinitionMap = new ConcurrentHashMap<String, BeanDefinition>();
    public DefaultBeanFactory() {

    }

    public BeanDefinition getBeanDefinition(String beanID) {
        return this.beanDefinitionMap.get(beanID);
    }

    public void registerBeanDefinition(String beanID, BeanDefinition bd) {
        this.beanDefinitionMap.put(beanID, bd);
    }

    public Object getBean(String beanID) {
        BeanDefinition beanDefinition = this.getBeanDefinition(beanID);
        if (beanDefinition == null){
            throw new BeanCreationException("BeanDefinition does not exist");
        }
        if(beanDefinition.isSingleton()){
            Object bean = this.getSingleton(beanID);
            if(bean == null){
                bean = createBean(beanDefinition);
                this.registerSingleton(beanID, bean);
            }
            return bean;
        }
        return createBean(beanDefinition);

    }
    private Object createBean(BeanDefinition beanDefinition) {
        //创建实例
        Object bean = instantiateBean(beanDefinition);
        //设置属性
        populateBean(beanDefinition, bean);

        return bean;

    }
    public Object instantiateBean(BeanDefinition beanDefinition){

        if(beanDefinition.hasConstructorArgumentValues()){
            ConstructorResolver resolver = new ConstructorResolver(this);
            return resolver.autowireConstructor(beanDefinition);
        }else {
            ClassLoader classLoader = this.getBeanClassLoader();
            String beansClassName = beanDefinition.getBeansClassName();
            try {
                Class<?> clz = classLoader.loadClass(beansClassName);
                return clz.newInstance();
            } catch (Exception e) {
                throw new BeanCreationException("create bean for "+ beansClassName +" failed",e);
            }
        }


    }

    protected void populateBean(BeanDefinition bd, Object bean){

        for(BeanPostProcessor processor : this.getBeanPostProcessors()){
            if(processor instanceof InstantiationAwareBeanPostProcessor){
                ((InstantiationAwareBeanPostProcessor)processor).postProcessPropertyValues(bean, bd.getID());
            }
        }


        List<PropertyValue> pvs = bd.getPropertyValues();

        if (pvs == null || pvs.isEmpty()) {
            return;
        }

        BeanDefinitionValueResolver valueResolver = new BeanDefinitionValueResolver(this);
        SimpleTypeConverter converter = new SimpleTypeConverter();
        try{
            for (PropertyValue pv : pvs){
                String propertyName = pv.getName();
                Object originalValue = pv.getValue();
                Object resolvedValue = valueResolver.resolveValueIfNecessary(originalValue);

                BeanInfo beanInfo = Introspector.getBeanInfo(bean.getClass());
                PropertyDescriptor[] pds = beanInfo.getPropertyDescriptors();
                for (PropertyDescriptor pd : pds) {
                    if(pd.getName().equals(propertyName)){
                        Object convertedValue = converter.convertIfNecessary(resolvedValue, pd.getPropertyType());
                        pd.getWriteMethod().invoke(bean, convertedValue);
                        break;
                    }
                }


            }
        }catch(Exception ex){
            throw new BeanCreationException("Failed to obtain BeanInfo for class [" + bd.getBeansClassName() + "]", ex);
        }
    }
    public void setBeanClassLoader(ClassLoader beanClassLoader) {
        this.beanClassLoader = beanClassLoader;
    }

    public ClassLoader getBeanClassLoader() {
        return (this.beanClassLoader != null ? this.beanClassLoader : ClassUtils.getDefaultClassLoader());
    }

    public Object resolveDependency(DependencyDescriptor descriptor) {
        Class<?> typeToMatch = descriptor.getDependencyType();
        for(BeanDefinition bd: this.beanDefinitionMap.values()){
            //确保BeanDefinition 有Class对象
            resolveBeanClass(bd);
            Class<?> beanClass = bd.getBeanClass();
            if(typeToMatch.isAssignableFrom(beanClass)){
                return this.getBean(bd.getID());
            }
        }
        return null;
    }
    public void resolveBeanClass(BeanDefinition bd) {
        if(bd.hasBeanClass()){
            return;
        } else{
            try {
                bd.resolveBeanClass(this.getBeanClassLoader());
            } catch (ClassNotFoundException e) {
                throw new RuntimeException("can't load class:"+bd.getBeansClassName());
            }
        }
    }

    public void addBeanPostProcessor(BeanPostProcessor postProcessor){
        this.beanPostProcessors.add(postProcessor);
    }
    public List<BeanPostProcessor> getBeanPostProcessors() {
        return this.beanPostProcessors;
    }
}
