package com.javapex.test.v2;

import com.javapex.beans.BeanDefinition;
import com.javapex.beans.PropertyValue;
import com.javapex.beans.factory.config.RuntimeBeanReference;
import com.javapex.beans.factory.support.DefaultBeanFactory;
import com.javapex.beans.factory.xml.XmlBeanDefinitionReader;
import com.javapex.core.io.ClassPathResource;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

public class BeanDefinitionTestV2 {

    @Test
    public void testGetBeanDefinition() {

        DefaultBeanFactory factory = new DefaultBeanFactory();
        XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(factory);
        reader.loadBeanDefinitions(new ClassPathResource("petstore-v2.xml"));
        BeanDefinition bd = factory.getBeanDefinition("petStore");

        List<PropertyValue> pvs = bd.getPropertyValues();
        Assert.assertTrue(pvs.size() == 4);

        {
            PropertyValue pv = this.getPropertyValue("accountDao", pvs);
            Assert.assertNotNull(pv);
            Assert.assertTrue(pv.getValue() instanceof RuntimeBeanReference);
        }

        {
            PropertyValue pv = this.getPropertyValue("itemDao", pvs);
            Assert.assertNotNull(pv);
            Assert.assertTrue(pv.getValue() instanceof RuntimeBeanReference);
        }

    }

    private PropertyValue getPropertyValue(String name,List<PropertyValue> pvs){
        //不希望有顺序
        for(PropertyValue pv : pvs){
            if(pv.getName().equals(name)){
                return pv;
            }
        }
        return null;
    }
}
