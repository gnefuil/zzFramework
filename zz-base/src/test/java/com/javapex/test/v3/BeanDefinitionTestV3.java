package com.javapex.test.v3;

import com.javapex.beans.BeanDefinition;
import com.javapex.beans.factory.config.RuntimeBeanReference;
import com.javapex.beans.factory.config.TypedStringValue;
import com.javapex.beans.factory.support.ConstructorArgument;
import com.javapex.beans.factory.support.DefaultBeanFactory;
import com.javapex.beans.factory.xml.XmlBeanDefinitionReader;
import com.javapex.core.io.ClassPathResource;
import com.javapex.core.io.Resource;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

public class BeanDefinitionTestV3 {

    @Test
    public void testConstructorArgument() {

        DefaultBeanFactory factory = new DefaultBeanFactory();
        XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(factory);
        Resource resource = new ClassPathResource("petstore-v3.xml");
        reader.loadBeanDefinitions(resource);

        BeanDefinition bd = factory.getBeanDefinition("petStore");
        Assert.assertEquals("com.javapex.service.v3.PetStoreService", bd.getBeansClassName());

        ConstructorArgument args = bd.getConstructorArgument();
        List<ConstructorArgument.ValueHolder> valueHolders = args.getArgumentValues();

        Assert.assertEquals(3, valueHolders.size());

        RuntimeBeanReference ref1 = (RuntimeBeanReference)valueHolders.get(0).getValue();
        Assert.assertEquals("accountDao", ref1.getBeanName());
        RuntimeBeanReference ref2 = (RuntimeBeanReference)valueHolders.get(1).getValue();
        Assert.assertEquals("itemDao", ref2.getBeanName());

        TypedStringValue strValue = (TypedStringValue)valueHolders.get(2).getValue();
        Assert.assertEquals( "1", strValue.getValue());

    }

}
