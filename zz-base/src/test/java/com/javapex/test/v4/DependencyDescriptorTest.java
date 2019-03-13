package com.javapex.test.v4;

import com.javapex.beans.factory.config.DependencyDescriptor;
import com.javapex.beans.factory.support.DefaultBeanFactory;
import com.javapex.beans.factory.xml.XmlBeanDefinitionReader;
import com.javapex.core.io.ClassPathResource;
import com.javapex.core.io.Resource;
import com.javapex.dao.v4.AccountDao;
import com.javapex.service.v4.PetStoreService;
import org.junit.Assert;
import org.junit.Test;

import java.lang.reflect.Field;

public class DependencyDescriptorTest {
    @Test
    public void testResolveDependency() throws Exception{

        DefaultBeanFactory factory = new DefaultBeanFactory();
        XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(factory);
        Resource resource = new ClassPathResource("petstore-v4.xml");
        reader.loadBeanDefinitions(resource);

        Field f = PetStoreService.class.getDeclaredField("accountDao");
        DependencyDescriptor descriptor = new DependencyDescriptor(f,true);
        Object o = factory.resolveDependency(descriptor);
        Assert.assertTrue(o instanceof AccountDao);
    }
}
