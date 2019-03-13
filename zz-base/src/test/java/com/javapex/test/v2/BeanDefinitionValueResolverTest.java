package com.javapex.test.v2;

import com.javapex.beans.factory.config.RuntimeBeanReference;
import com.javapex.beans.factory.config.TypedStringValue;
import com.javapex.beans.factory.support.BeanDefinitionValueResolver;
import com.javapex.beans.factory.support.DefaultBeanFactory;
import com.javapex.beans.factory.xml.XmlBeanDefinitionReader;
import com.javapex.core.io.ClassPathResource;
import com.javapex.dao.v2.AccountDao;
import org.junit.Assert;
import org.junit.Test;

public class BeanDefinitionValueResolverTest {

    @Test
    public void testResolveRuntimeBeanReference() {
        DefaultBeanFactory factory = new DefaultBeanFactory();//factory可以把类的名称变成类的实例
        XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(factory);
        reader.loadBeanDefinitions(new ClassPathResource("petstore-v2.xml"));

        BeanDefinitionValueResolver resolver = new BeanDefinitionValueResolver(factory);

        RuntimeBeanReference reference = new RuntimeBeanReference("accountDao");
        Object value = resolver.resolveValueIfNecessary(reference);

        Assert.assertNotNull(value);
        Assert.assertTrue(value instanceof AccountDao);
    }
    @Test
    public void testResolveTypedStringValue() {
        DefaultBeanFactory factory = new DefaultBeanFactory();
        XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(factory);
        reader.loadBeanDefinitions(new ClassPathResource("petstore-v2.xml"));

        BeanDefinitionValueResolver resolver = new BeanDefinitionValueResolver(factory);

        TypedStringValue stringValue = new TypedStringValue("test");
        Object value = resolver.resolveValueIfNecessary(stringValue);
        Assert.assertNotNull(value);
        Assert.assertEquals("test", value);

    }
}
